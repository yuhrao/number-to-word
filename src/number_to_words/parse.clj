(ns number-to-words.parse
  (:require [number-to-words.specs :as n-specs]
            [clojure.string :as str]))

(defn number->digit-groups [number]
  (->> (format "%09d" number)
       (partition 3 3)
       (map (fn [group] (map #(Character/getNumericValue %) group)))))

(defn get-after-hundreds [tens units]
  (when-not (and (zero? tens) (zero? units))
    (let [raw-ten (* tens 10)]
      (if (= tens 1)
        (do
          (->> (+ raw-ten units)
               n-specs/get-natural-name
               #_((fn [v] (prn v) v))))
        (->> [raw-ten units]
             (map n-specs/get-natural-name)
             (filter (comp not nil?))
             (str/join " "))))))

(defn get-hundreds [hundreds tens units]
  (when-not (zero? hundreds)
    (let [and-sentence (when (or (> tens 0) (> units 0)) "and")
          hundreds-word (n-specs/get-natural-name hundreds)]
      (->> [hundreds-word "hundred" and-sentence]
           (filter (comp not nil?))
           (str/join " " )))))

(defn group->words [power group]
  (when-not (every? zero? group)
    (let [[_ tens units]   group
          power-complement (n-specs/get-powers-of-tens power)]
      (->> [(apply get-hundreds group)
            (get-after-hundreds tens units)
            power-complement]
           (filter (comp not nil?))
           (str/join " ")))))

(comment
  (letfn [(groups->words [groups]
            (map-indexed group->words groups))]
    (let [sample 123456000]
      (->> sample
           number->digit-groups
           groups->words
           (filter (comp not empty?))
           (str/join " and ")
           #_(str/trim)))))
