# E-pood: ostukorv ja tellimused

Java käsurearakendus väikese e-poe jaoks (TAK25, Olavi Keerd).

Kasutaja saab:
- vaadata tootekataloogi, sorteerida tooteid hinna järgi, filtreerida kategooria järgi ja otsida nime järgi;
- lisada tooteid ostukorvi ja neid sealt eemaldada;
- vormistada tellimuse koos sooduskoodiga (`SOODUS10` = -10%, `KINK5` = -5 €);
- vaadata tellimuste ajalugu.

Füüsilistel toodetel on laoseis ja saatmiskulu (kaalu järgi, alates 50 € tasuta),
digitaalsetel toodetel laoseisu piirangut ega saatmiskulu pole.

## Käivitamine

Vaja on Java 17 või uuemat (JDK).

**IntelliJ IDEA:** ava kaust `epood`, märgi `src` kaust *Sources Root*-iks ja käivita `ShopApp`.

**Käsurealt** (projekti kaustas):

```bash
javac -encoding UTF-8 -d out src/*.java
java -cp out ShopApp
```

Kui täpitähed kuvatakse Windowsi konsoolis valesti, käivita enne `chcp 65001`.

## Ülesehitus

| Klass | Ülesanne |
|---|---|
| `Product` | Abstraktne ülemklass: kood, nimi, kategooria, hind |
| `PhysicalProduct` | Alamklass: laoseis, kaal, saatmiskulu |
| `DigitalProduct` | Alamklass: faili suurus, piiramatu laoseis, tasuta saatmine |
| `Discount` | Liides allahindlustele |
| `PercentageDiscount`, `FixedDiscount` | Protsendiline ja kindla summa allahindlus |
| `CartItem`, `ShoppingCart` | Ostukorvi rida ja ostukorv |
| `Customer`, `Order` | Klient ja vormistatud tellimus |
| `OutOfStockException` | Oma erand, kui kaupa pole piisavalt |
| `Shop` | Kataloog, otsing/filter/sort, sooduskoodid, tellimuse vormistamine |
| `ConsoleInput` | Sisendi lugemine ja kontroll |
| `ShopApp` | `main` meetod ja menüü |

## OOP põhimõtted koodis

- **Kapseldamine** – kõik väljad on `private`; hinda muudab ainult `Product.setPrice()`, mis keelab
  negatiivse hinna; laoseisu vähendab ainult `PhysicalProduct.removeFromStock()`. `getItems()`,
  `getOrders()` jt tagastavad koopia, et klassi sisemist listi ei saaks väljast muuta.
- **Pärilus** – `PhysicalProduct` ja `DigitalProduct` pärivad `Product`-ist ühised väljad ja meetodid.
- **Polümorfism** – `ShoppingCart.getShippingCost()` kutsub iga toote `getShippingCost()`; füüsiline
  toode arvutab kaalu järgi, digitaalne tagastab 0. Sama `Discount.apply()` töötab eri moodi
  `PercentageDiscount` ja `FixedDiscount` puhul.
- **Abstraktsioon** – `Product` on abstraktne klass (ühised andmed + abstraktsed meetodid),
  `Discount` on liides (ainult „leping“, ühiseid andmeid pole).

## Andmestruktuurid

| Kus | Struktuur | Miks |
|---|---|---|
| `Shop.catalog` | `LinkedHashMap<String, Product>` | toode leitakse koodi järgi kohe, kood on unikaalne, kuvamise järjekord säilib |
| `Shop.categories` | `TreeSet<String>` | kategooriad ilma kordusteta ja tähestiku järjekorras |
| `Shop.promoCodes` | `HashMap<String, Discount>` | kiire otsing koodi järgi, järjekord pole oluline |
| `Shop.orders`, `ShoppingCart.items` | `ArrayList` | järjekord on oluline, alati käiakse kõik läbi |

Sorteerimine: `Comparator.comparingDouble(Product::getPrice)` (`Shop.getProductsSortedByPrice`).
