package com.mtesitoo.backend.service;

import com.mtesitoo.backend.model.Category;
import com.mtesitoo.backend.service.logic.ICategoryServiceResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nan on 9/19/2015.
 */
public class CategoryServiceResponse implements ICategoryServiceResponse {
    public List<Category> parseResponse(String response) throws JSONException {
        JSONObject jsonResponse = new JSONObject(response);
        JSONArray jsonCategories = jsonResponse.getJSONArray("categories");
        List<Category> result = new ArrayList<>(jsonCategories.length());

        for (int i = 0; i < jsonCategories.length(); ++i) {
            JSONObject jsonCategory = jsonCategories.getJSONObject(i);
            Category category = new Category(Integer.parseInt(
                    jsonCategory.getString("category_id")),
                    jsonCategory.getString("name"));

            result.add(category);
        }

        return result;
    }
}