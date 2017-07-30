package com.indexer.happyshop.model;

/**
 * Created by indexer on 26/7/17.
 * "id": 190,
 "name": "Original Panther Black",
 "category": "Tools",
 "price": 3000,
 "img_url": "http://luxola-assets-staging-nemesis.s3.amazonaws.com/images/pictures/6183/default_f6d5b9cedcfb28b2402b914735bd3e9bf886183c_1408961000_TT_Blackpanther_1.jpg",
 "description": "Lorem ipsum dolor sit amet Original Panther Black product description consectetur adipiscing elit, sed do eiusmod tempor incididunt.",
 "under_sale": false
 */

public interface Product {
  int getId();
  String getName();
  String getDescription();
  int getPrice();
  String getImg_url();
  String getCategory();
  boolean isUnder_sale();
  boolean isInCash();
}