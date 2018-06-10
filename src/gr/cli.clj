(ns gr.cli
  (:require [clojure.string :as str]))

;;
;; command line argument validation
;;

(def ^:const args-server #{"-s" "--server"})
(def ^:const args-file #{"-f" "--file"})
(def ^:const args-sort #{"-o" "--sort"})
(def ^:const args-help #{"-h" "--help"})
(def ^:const valid-sort-values #{1 2 3})

(def ^:const usage
  "Usage statement"
  (str/join "\n"
            ["Usage: gr help server file sort"
             "   help: -h, --help  Displays usage statement. Ignores any other args."
             "   server: -s, --server  Runs as a web server.  Ignores file and sort args."
             "                         If not provided, must specify file and sort args to run in script mode."
             "   file: -f, --file <path-to-file> Full path to file to load."
             "   sort: -s, --sort n  where n is 1 - sort by Gender (Females first) asc then by LastName asc"
             "                                  2 - sort by DateOfBirth asc"
             "                                  3 - sort by LastName desc"]))

(defn has-args-option? [valid-args args]
  "Indicates whether any of the valid-args is present in args"
  (not (nil? (some valid-args args))))

(defn arg-pos [valid-args args]
  "Returns position of found valid-arg in args or -1"
  (letfn [(f [as pos]
            (if (empty? as)
              -1
              (if (valid-args (first as))
                pos
                (f (rest as) (inc pos)))))]
    (f args 0)))

(defn arg-value [valid-args args]
  "Returns the valid-args value from args or nil, ie. --sort 1 will return 1 when looking for the sort arg value"
  (let [pos (arg-pos valid-args args)]
    (if (<= 0 pos (- (count args) 2))
      (nth args (inc pos))
      nil)))

(defn parse-args [args]
  "Sets the values of the command line args in the map. nil if non-existent or invalid"
  {:server (has-args-option? args-server args)
   :file   (arg-value args-file args)
   :sort   (let [val (arg-value args-sort args)]
             (valid-sort-values (if (instance? String val) (Integer/parseInt val) val)))
   :help   (has-args-option? args-help args)})
