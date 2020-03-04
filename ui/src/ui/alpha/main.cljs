(ns ui.alpha.main
  (:require
   [cljs.repl :as repl]
   [cljs.pprint :as pp]
   [reagent.core :as r]
   [re-frame.core :as rf]
   [re-pressed.core :as rp]
   [devtools.core :as devtools]

   [ui.alpha.evs :as evs]
   [ui.alpha.routes :as routes]
   [ui.alpha.config :as config]
   [ui.alpha.subs :as subs]
   [ui.alpha.view :refer [view]]

   [ui.alpha.repl]
   [ui.alpha.tests]
   [common.alpha.game]
   [common.alpha.spec]
   [common.alpha.tests]
   [ui.other-tests]))

(devtools/install!)
#_(enable-console-print!)

(defn mount-root []
  (rf/clear-subscription-cache!)
  (r/render [view]
            (.getElementById js/document "ui")))

(defn dev-setup []
  (when config/debug?
    (js/document.addEventListener "keyup" (fn [e]
                                            (when (= (.. e -key) "r")
                                              (mount-root)
                                              (prn "r/render"))))
    #_(println "dev mode" config/debug?)))

(defn ^:export main []
  (routes/app-routes)
  (rf/dispatch-sync [::evs/initialize-db])
  (rf/dispatch-sync [::rp/add-keyboard-event-listener "keydown"])
  (dev-setup)
  (mount-root))

(defn ^:dev/after-load after-load []
  #_(js/console.log "--after load")
  (mount-root))