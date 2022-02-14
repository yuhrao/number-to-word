(ns number-to-words.utils
  (:require [number-to-words.specs :as n-specs]
            [clojure.string :as str]))

(defn- not-zero? [n]
  (not (zero? n)))

(defn number->digit-groups [number]
  (->> (format "%09d" number)
       (partition 3 3)
       (map (fn [group] (map #(Character/getNumericValue %) group)))))

(defn get-after-hundreds [tens units]
  (when (and (not-zero? tens) (not-zero? units))
    (let [and-sentence (when (< tens 0) (< 0 units) "and")
          raw-ten (* tens 10)]
      (if (= tens 1)
        (->> (+ raw-ten units)
             n-specs/get-natural-name
             (str and-sentence))
        (->> [raw-ten units]
             (map n-specs/get-natural-name)
             (filter (comp not nil?))
             (str/join " "))))))

(comment
  (letfn [
          (group->word [power group]
            (when (every? not-zero? group)
              (let [[hundreds tens units] group
                    power-complement      (n-specs/get-powers-of-tens power)
                    hundred-complement    (when (not (zero? hundreds))
                                            "hundred")]
                (str/join " "
                          [(n-specs/get-natural-name hundreds)
                           hundred-complement
                           (get-after-hundreds tens units)
                           power-complement]))))
          (groups->words [groups]
            (map-indexed group->word groups))]
    (let [sample 123456780]
      (->> sample
           number->digit-groups
           groups->words
           (filter (comp not empty?))
           (str/join " and ")
           (str/trim))))

  )
