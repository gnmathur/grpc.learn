# Notes

* Using `sint32` and `sint64` are more efficient in terms of encoding negative values. 
* All fields are optional in proto3
* Proto is more efficient than JSON - 
** Serialized size is smaller in proto
** Serialization and deserialization is faster in a tuple of comparable data set
* `buildPartial` can be ignored in Proto3. Just use `build`
* V2 of a message might have new fields V1 parser will still parse it. However, V2 message should not modify the field
  types of V1 fields at the same proto index. Note that you could potentially change the field name at the index.
  However, you should never change the _type_
```
user.v1.proto

message User {
    string name = 1
    int32 age = 2
    string hair_color = 3
}

user.v2.proto

message User {
    string name = 1;
    int32 languages_known = 2;
    string hair_color = 3;
}
```
In this example v1 and v2 parsers will be able to parse each other's messages, even though the field name at index 2 has
changed. However, for v1 parser, languages_known will show up as age, and vice versa :-)
