`@one-to-one mapping(cascade = CascadeType.PERSIST)`
saving owner makes saving inverse side as well.
no need to use save method for both owner & inverse side. Just use save for owner and inverse side will be saved automatically because of CascadeType.PERSIST

inverse side se owner ko feth karne ke liye
@one-to-one mapping mein foriegn kahi bhi bana lo, ownermein bana lo ya inverse side mein bana ko koi farak nahi padta

but

one-to-many mapping mein always many side contains foriegn key and one who contains foreign key will become owner and other one will be inverse.

---------------------------------

`@one-to-one(mappedBy = "address", fetch = FetchType.LAZY)` // address is the reference we used in Student class
mappedBy says dont create foriegn key in Address table because Student class (table) is having foriegn key through:
@OneToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "address_id")
private Address address; // here address is the reference

Agar owner side se inverse side ka data lana hai toh proxy object banega first inverse side iss proxy object mein only foreign key ki value hogi and baaki sab field inverse side ki null hogi.

jab hum actually other fields access karenge inverse side ki toh ek actual sql query execute hogi and proxy object update ho jayega with all the actual values.

Agar inverse side se owner side call karenge, although humne inverse side mein owner ko lazy fetch rakha hai phir bhi only inverse side fetch karne se owner side ka actual object (no proxy) ban jayega with all the fields having actual values because humne inverse mein foriegn key nahi rakha hai owner ke liye & inverse side ko kabhi pata nahi chalega ki konsa address kiss student ko belong karta hai isliye actual sql query execute ho jayega owner (Student) ke liye with just loading address.

Inverse side mein `(@one-to-one(mappedBy = "address", fetch = FetchType.LAZY))` is as same as `@one-to-one(mappedBy = "address")` (now `fetch = FetchType.EAGER` by default) because we are anyway making call to owner regardless of asking for owner or not after fetching address (inverse side).
