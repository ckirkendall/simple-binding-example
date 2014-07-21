(ns binding-example.client
    (:require [enfocus.core :as ef]
              [enfocus.bind :as bind]
              [enfocus.effects :as effects]
              [enfocus.events :as events]
              [clojure.browser.repl :as repl])
    (:use-macros [enfocus.macros :only [deftemplate defsnippet defaction]]))

(declare add-fruit)

;;************************************************
;; Dev stuff
;;************************************************
(def dev-mode true)

(defn repl-connect [] 
 (when dev-mode
   (repl/connect "http://localhost:9000/repl")))

;;************************************************
;; APP DATA
;;************************************************
(def fruits ["Apple" "Pear" "Grape" "Peach" "Tomato"
             "Mango" "Fig" "Bannana" "Pineapple"])

(def simple-data (atom (map #(do [(str %1)
                                  (get fruits (rand-int (count fruits)))
                                  (rand-int 100)]) (reverse (range 10)))))


;;************************************************
;; SIMPLE DATA - snippets and tempaltes
;;************************************************


(defn add-simple-fruits []
  (let [{:keys [name quantity]} (ef/from "#simple-update-form" (ef/read-form))]
    (swap! simple-data #(conj % [(str (count %)) name quantity]))))

(defn remove-simple-fruit [id]
  (swap! simple-data (fn [data] (filter #(not= (first %) id) data))))


;note selectors can be vectors or bare strings
(defsnippet fruit-row :compiled "public/index.html"  ["#simple-fruit > tbody > *:first-child"]
  [[id name quantity]]
  [".id"] (ef/content id)
  [".name"] (ef/content name)
  [".quantity"]  (ef/content (str quantity))
  [".remove-btn"] (events/listen :click #(remove-simple-fruit id))) 
   

;;###############################################
;; SIMPLE DATA Binding Functions
;;###############################################

(defn display-fruit-table [node data]
  (ef/at node "tbody" (ef/content (map fruit-row data))))

;;************************************************
;; actions/navigation[
;;************************************************

(defaction init []
  "#simple-fruit" (bind/bind-view simple-data display-fruit-table)
  "#simple-add-btn" (events/listen :click add-simple-fruits))


;;************************************************
;; onload
;;************************************************

(set! (.-onload js/window)
      #(do
         (repl-connect)
         (init)))
