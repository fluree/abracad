(ns abracad.avro.compare
  "Generic data comparison implementation."
  {:private true}
  (:refer-clojure :exclude [compare])
  (:require [abracad.avro :as avro]
            [abracad.avro.util :as util]
            [abracad.avro.write :as write])
  (:import [abracad.avro ClojureData]
           [org.apache.avro Schema Schema$Field Schema$Field$Order Schema$Type]))

(declare compare)

(defn order-ignore?
  [^Schema$Field f]
  (identical? (.order f) Schema$Field$Order/IGNORE))

(defn order-ascending?
  [^Schema$Field f]
  (identical? (.order f) Schema$Field$Order/ASCENDING))

(defn compare-record ^long
  [x y ^Schema schema equals]
  (loop [fields (.getFields schema)]
    (if-let [[^Schema$Field f & fields] (seq fields)]
      (if (order-ignore? f)
        (recur fields)
        (let [schema (.schema f), name (keyword (.name f))
              x (avro/field-get x name), y (avro/field-get y name)
              result (compare x y schema equals)]
          (if (zero? result)
            (recur fields)
            (if (order-ascending? f)
              result
              (- result)))))
      0)))

(defn compare-enum ^long
  [x y ^Schema schema equals]
  (- (.getEnumOrdinal schema (name x))
     (.getEnumOrdinal schema (name y))))

(defn compare-union ^long
  [x y ^Schema schema equals]
  (let [ix (write/resolve-union* schema x)
        iy (write/resolve-union* schema y)]
    (if (= ix iy)
      (compare x y (-> schema .getTypes (.get ix)) equals)
      (- ix iy))))

(defn supercompare ^long
  [x y ^Schema schema equals]
  (._supercompare (ClojureData/get) x y schema equals))

(defn compare
  ^long [x y ^Schema schema equals]
  (if (identical? x y)
    0
    (util/case-enum (.getType schema)
      Schema$Type/RECORD (compare-record x y schema equals)
      Schema$Type/ENUM (compare-enum x y schema equals)
      Schema$Type/UNION (compare-union x y schema equals)
      #_ else (supercompare x y schema equals))))
