(ns workout-contest.models.data
  (:require [noir.cookies :as cookies]
            [clojure.java.jdbc :as jdbc])
  (:use [clojure.java.io]))

(def db {:classname "org.sqlite.JDBC"
         :subprotocol "sqlite"
         :subname "prod.db"})

(defn create-db
  []
  (jdbc/with-connection db
    (jdbc/create-table :usr
      [:user_id "varchar primary key"]
      [:name "varchar"]
      [:arbetsplats "varchar"])
    (jdbc/create-table :workouts
      [:id "integer primary key"]
      [:user_id "varchar"]
      [:workout_day "varchar"])))

(defn drop-db
  []
  (jdbc/with-connection db
    (jdbc/drop-table :usr)
    (jdbc/drop-table :workouts)))

(defn insert-prod-users
  [f]
  (let [users (with-open [r (reader f)]
                (doall
                  (for [line (line-seq r)]
                    (let [[name arbetsplats] (.split line ",")]
                      {:name name :arbetsplats arbetsplats}))))
        users (map #(assoc % :user_id (.toUpperCase 
                                        (Long/toString 
                                          (Math/abs 
                                            (hash (:name %))) 36))) 
                   users )]
    (jdbc/with-connection db
      (apply jdbc/insert-records :usr users))))


(defn get-user
  [user-id]
  (jdbc/with-connection db
    (jdbc/with-query-results
      res ["SELECT * FROM usr WHERE user_id = ?" user-id]
      (first res))))

(defn workout-dates
  "Returns a vector of strings representing the days the user has
   worked out."
  [user-id]
  (jdbc/with-connection db
    (jdbc/with-query-results
      res ["SELECT workout_day FROM workouts WHERE user_id = ?" user-id]
      (doall (map :workout_day res)))))

(defn get-score
  [user]
  (let [user-id (if (map? user)
                  (:user_id user)
                  user)]
    (count (workout-dates user-id))))

(defn valid-date?
  "To avoid cheating (and my own mistakes!)"
  [date]
  (let [[year month day :as date] (map #(Long/parseLong %)
                                       (drop 1
                                             (re-matches 
                                               #"(\d\d\d\d)-(\d\d)-(\d\d)"
                                               date)))]
    (cond
      (some nil? date) false
      (not= 2012 year) false
      (< day 1) false
      (< month 10) false
      (> month 11) false
      (and (= 10 month) (> day 31)) false
      (and (= 11 month) (> day 30)) false
      :else true)))


(defn date-clicked
  "Handles the case where the user has clicked a date. If already present
   in db, will be removed - otherwise it will be added."
  [user-id date]
  (jdbc/with-connection db
    (jdbc/with-query-results
      res ["SELECT * FROM workouts WHERE user_id = ? and workout_day = ?" user-id date]
      (if (empty? (doall res))
        (jdbc/insert-record :workouts
          {:user_id user-id :workout_day date})
        (jdbc/delete-rows :workouts
          ["user_id = ? and workout_day = ?" user-id date])))))


(defn top-list
  "Returns a map of users with their name, arbetsplats and score, sorted on score."
  []
  (jdbc/with-connection db
    (let [users (jdbc/with-query-results
                  res ["SELECT * FROM usr"]
                  (doall res))
          users (map 
                  (fn [user]
                    (assoc user :score (get-score user))) 
                  users)]
      (remove #(zero? (:score %)) (reverse (sort-by :score users))))))
