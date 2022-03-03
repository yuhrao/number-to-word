(ns number-to-words.parse
  (:require [number-to-words.client :as n-cli]
            [clojure.string :as str]))

(defn number->digit-groups [number]
  (->> (format "%09d" number)
       (partition 3 3)
       (map (fn [group] (map #(Character/getNumericValue %) group)))))

(defn get-after-hundreds [client group]
  (when-not (every? zero? group)
    (let [[tens units] group
          raw-ten (* tens 10)]
      (if (= tens 1)
        (->> (+ raw-ten units)
             (n-cli/get-natural-name client))
        (->> [raw-ten units]
             (map #(n-cli/get-natural-name client %1))
             (filter (comp not nil?))
             (str/join " "))))))

(defn get-hundreds [client [hundreds tens units]]
  (when-not (zero? hundreds)
    (let [and-sentence (when (or (> tens 0) (> units 0)) "and")
          hundreds-word (n-cli/get-natural-name client hundreds)]
      (->> [hundreds-word "hundred" and-sentence]
           (filter (comp not nil?))
           (str/join " " )))))

(defn group->words [client power group]
  (when-not (every? zero? group)
    (let [[_ tens units]   group
          power-complement (n-cli/get-powers-of-tens client power)]
      (->> [(get-hundreds client group)
            (get-after-hundreds client [tens units])
            power-complement]
           (filter (comp not nil?))
           (str/join " ")))))

(defn number->words
  "Take a number 'n' and convert it into words"
  [client n]
  (if (zero? n)
    "zero"
    (->> n
         number->digit-groups
         (map-indexed #(group->words client %1 %2))
         (filter (comp not empty?))
         (str/join " and "))))
