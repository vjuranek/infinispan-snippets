package org.infinispan.demo.pb;

message Book {
  required string title = 1;
  required string description = 2;
  required int32 publicationYear = 3;
}