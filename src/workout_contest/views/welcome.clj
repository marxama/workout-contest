(ns workout-contest.views.welcome
  (:require [workout-contest.views.common :as common]
            [noir.content.getting-started]
            [noir.cookies :as cookies]
            [noir.response :as resp])
  (:use [noir.core :only [defpage defpartial pre-route]]
        workout-contest.models.data
        [hiccup core form element page]))


(defn index-page [& {:keys [message]}]
  (common/layout (form-to [:post "/"]
                          (if message
                            [:p message])
                          (label "user-id" "Användar-ID: ")
                          (text-field "user-id")
                          (submit-button "Logga in"))))


(pre-route "/" []
           (if (get-user (cookies/get :user-id))
             (resp/redirect "/main")))

(defpage [:get "/"] []
  (index-page))

(defpage [:post "/"] {:keys [user-id]}
  (if (get-user user-id)
    (do 
      (cookies/put! :user-id {:value user-id #_:http-only #_true})
      (resp/redirect "/main"))
    (index-page :message "Login error!")))


(defpartial top-list-user
  [user]
  [:tr
   [:td (:name user) [:br] (:arbetsplats user)]
   [:td (:score user)]])

(defpartial top-list-html
  []
  (let [users (top-list)]
    [:table#toplist 
     (for [user users]
       (top-list-user user))]))

(pre-route "/main" []
           (if-not (get-user (cookies/get :user-id))
             (resp/redirect "/")))


(defn main-page
  []
  (let [user (get-user (cookies/get :user-id))]
    (common/layout
      [:p (str "Välkommen, " (:name user) "!")
       [:br]
       "Du har " [:span#scorefield (get-score user)] " poäng."]
      [:p (link-to "/logout" "Logga ut")]
      [:h3 "Dina aktiva dagar"]
      [:div [:span#october] [:span#november]]
      [:br]
      [:h3 "Topplista"]
      (top-list-html))))


(defpage [:get "/main"] []
  (main-page))

(defpage [:post "/main"] []
  (main-page))

(defpage [:post "/dates"] []
  (resp/json (workout-dates (cookies/get :user-id))))

(defpage [:post "/setdate"] {:keys [date]}
  (if (valid-date? date)
    (date-clicked (cookies/get :user-id) date)
    {:status 400
     :body ""}))

(defpage "/logout" []
  (cookies/put! :user-id {:value "" :max-age 0})
  (resp/redirect "/"))