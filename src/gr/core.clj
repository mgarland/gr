(ns gr.core
  (:gen-class))

;;
;; command line argument validation
;;
(defn usage []
  (do
    (println "Usage: file-name sort-option")
    (println "   file-name: full path to file to load")
    (println "   sort-option: 1 - sort by Gender asc then by LastName asc")
    (println "                2 - sort by DateOfBirth asc")
    (println "                3 - sort by LastName desc")))

(defn validate-sort-option [val]
  (#{1 2 3} val))



(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [[file-name sort-option] args
        is-sort-option-valid? (validate-sort-option sort-option)]
    (if is-sort-option-valid?
        nil
        (usage))))


