/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.indexer.happyshop;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;
import butterknife.internal.Utils;
import com.google.gson.annotations.Expose;
import com.indexer.happyshop.database.entity.ProductEntity;
import com.indexer.happyshop.model.Product;
import com.indexer.happyshop.viewmodel.ProductListViewModel;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest extends InstrumentationTestCase {
  private MockWebServer server;

  @Before
  public void setUp() throws Exception {
    super.setUp();
    server = new MockWebServer();
    server.start();
    injectInstrumentation(InstrumentationRegistry.getInstrumentation());
  }

  @Test
  public void readAppAsset() throws Exception {
    Context ctx = InstrumentationRegistry.getTargetContext();
    InputStream is = ctx.getResources().getAssets().open("product.json");
    String s = readTextStream(is);
    Assert.assertNotNull(s);
  }

  @Test
  public void readDatabase() throws Exception{
    LiveData<List<ProductEntity>> products = getProductListViewModel().getProducts();
    Assert.assertNotNull(products);
  }

  public static String readTextStream(InputStream inputStream) throws Exception {
    ByteArrayOutputStream result = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    int length;
    while ((length = inputStream.read(buffer)) != -1) {
      result.write(buffer, 0, length);
    }
    return result.toString("UTF-8");
  }

  @Rule
  public ActivityTestRule<MainActivity> mActivityRule =
      new ActivityTestRule(MainActivity.class);

  @Test
  public void testProductIsShownInRecyclerView() throws Exception {
    Context ctx = InstrumentationRegistry.getTargetContext();
    InputStream is = ctx.getResources().getAssets().open("product.json");
    String s = readTextStream(is);
    server.enqueue(new MockResponse()
        .setResponseCode(200)
        .setBody(s));

    Intent intent = new Intent();
    mActivityRule.launchActivity(intent);

    onView(withId(R.id.product_list)).check(
        matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
  }

  @Test
  public void clickOnFirstItem_opensComments() {
    // When clicking on the first product
    onView(withId(R.id.product_list))
        .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
  }

  private ProductListViewModel getProductListViewModel() {
    MainActivity activity = mActivityRule.getActivity();

    return ViewModelProviders.of(activity)
        .get(ProductListViewModel.class);
  }
}