package com.takatsuka.web.math;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.HashMap;
import java.util.Optional;

@RestController
@CrossOrigin
public class MathController {

  private final MathService mathService;
  private final String BASE_URL = "/math/";
  private static final HashMap<String, Bucket> BUCKETS = new HashMap<>();
  private static final String REQUEST_OVERLOAD_MSG =
      "You're making requests too quickly! Wait a little before making another.";

  // Refill one request every 6 seconds.
  private static final Bandwidth BUCKET_BANDWIDTH =
      Bandwidth.classic(1, Refill.intervally(1L, Duration.ofSeconds(3L)));

  public MathController(MathService mathService) {
    this.mathService = mathService;
  }

  @GetMapping(BASE_URL + "evaluate")
  public String evaluate(
      @RequestParam(name = "expression") String expression,
      @RequestParam(name = "compPrecision", required = false) int computationPrecision,
      @RequestParam(name = "dispPrecision", required = false) int displayPrecision,
      HttpServletRequest request) {
    Bucket bucket = getBucketOrCreate(request.getRemoteAddr());
    // TODO determine if the parameters will be null or 0
    MathSettings mathSettings =
        new MathSettings(
            Optional.ofNullable(displayPrecision), Optional.ofNullable(computationPrecision));

    if (bucket.tryConsume(1L)) {
      return mathService.evaluateExpression(expression, mathSettings);
    } else {
      return REQUEST_OVERLOAD_MSG;
    }
  }

  private Bucket getBucketOrCreate(String address) {
    Bucket bucket;
    if (BUCKETS.containsKey(address)) {
      bucket = BUCKETS.get(address);
    } else {
      bucket = Bucket4j.builder().addLimit(BUCKET_BANDWIDTH).build();
      BUCKETS.put(address, bucket);
    }
    return bucket;
  }
}
