(ns workout-contest.views.common
  (:use [noir.core :only [defpartial]]
        [hiccup.page :only [include-css include-js html5]]))

(defpartial info []
  [:h1 "Friskvårdstävling"]
  [:hr {:size 10 :color "#FF0000"}]
  [:p#bla "Hösten närmar sig med stormsteg och för att lysa upp i höstmörkret anordnas en friskvårdstävling. Tävlingen är individuell och kommer att pågå i två månader, från och med första oktober till sista november."]
  [:p "Varför är det viktigt med friskvård och hälsoarbete?"]
  [:p "En arbetsplats är beroende av personalens välbefinnande. Personalen är vår viktigaste resurs. Det är inte maskiner som utför vårt jobb, utan människor och då måste vi som arbetsgivare ge medarbetarna förutsättningar för ett bra välbefinnande. Sambandet mellan ett aktivt hälsoarbete, nöjda medarbetare och ökad lönsamhet har aldrig varit tydligare. Vi vill underlätta för våra medarbetare att finna balans i tillvaron och hälsoarbetet är en del."]
  [:p "Om vi arbetsgivare på ett aktivt sätt ger våra anställda förutsättningar för att bibehålla och/eller förbättra sin hälsa, bidrar detta till en mer välmående medarbetare och ett större engagemang och därigenom friskare och gladare medarbetare som kan smitta av sig i vårt dagliga arbete – som kommer våra gäster och besökare till godo!"]
  [:p "Hälsa är mer än bara motion. Hälsa är rörelse, glädje, vila, bra mat och att hitta glädjeämnen i vardagen."]
  [:p "Ett effektivt hälsoarbete är en god grund för ökad hälsa på arbetsplatsen. Därför är det viktigt med en aktiv hälsopolitik som ökar möjligheterna för medarbetarna att välja alternativ som främjar hälsa. Hälsoarbetet ska inom ramen för den ordinarie verksamheten arbeta förebyggande, ett hälsofrämjande förhållningssätt och ge positiva effekter för såväl individ som organisation."]
  
  [:h2 "HÖSTENS FRISKVÅRDSTÄVLING"]
  [:p "Friskvårdstävlingen riktar sig till medarbetarna på [diverse arbetsplatser]"]
  [:p "Du samlar ett poäng per dag genom 30 minuters sammanhängande fysisk aktivitet utöver ert vanliga arbete under två månader, exempelvis gym, gruppträning, promenad, cykling etc."]
  [:p "För att vara med och tävla logga in med ditt användar-ID, klicka i de datum då du samlat in 30 minuters sammanhängande fysisk aktivitet. Dina poäng ser du högst upp i högra hörnet. Under kalendrarna kan du se en topplista över samtliga medarbetare."]
  
  [:h2 "PRISER"]
  [:table#awards
   [:tr
    [:td "1:a pris"]
    [:td "Fin överraskning"]]
   [:tr
    [:td "2:a pris"]
    [:td "Nästan lika fin överraskning"]]]
  [:p "Vid frågor och funderingar kontakta XXX tel. 012-345 67 89 eller via mail: mail@mail.com."]
  [:p]
  [:p "Vi ser fram emot att aktivera oss med er!"])

(defpartial layout [& content #_{:keys [content js-includes css-includes]}]
  (html5
    [:head
     [:title "Friskvårdstävling"]
     (include-css "datepicker/css/mdp.css")
     (include-css "datepicker/css/prettify.css")
     (include-js "datepicker/js/jquery-1.7.2.js")
     (include-js "datepicker/js/jquery.ui.core.js")
     (include-js "datepicker/js/jquery.ui.datepicker.js")
     (include-js "datepicker/jquery-ui.multidatespicker.js")
     (include-js "datepicker/js/prettify.js")
     (include-js "datepicker/js/lang-css.js")
     (include-js "js/json.js")
     (include-js "js/js.js")
     (include-css "/css/reset.css")
     [:meta {:charset "utf-8"}]]
    [:body
     [:div#wrapper
      [:div#header {:align "center"} [:img {:src "img/orebrose.gif"}]]
      [:div#contents
       [:div#left (info)]
       [:div#right content]]]]))
