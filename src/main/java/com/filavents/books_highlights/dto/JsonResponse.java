package com.filavents.books_highlights.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonResponse {

  private final boolean success;
  private final List<?> objectList;

  public JsonResponse(boolean success, List<?> bookList) {
    this.success = success;
    this.objectList = bookList;
  }

  public Map<String, Object> asMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("success", success);
    map.put("data", this.objectList);
    return map;
  }


}
