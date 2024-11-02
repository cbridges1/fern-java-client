package com.guidewire.ccs.utils;

import java.security.SecureRandom;

public class Helper {

  public static long generateRandomUnsignedLong() {
    // Use SecureRandom for better randomness
    SecureRandom random = new SecureRandom();

    // Generate a random long value
    long high = random.nextLong(); // high 32 bits
    long low = random.nextLong();   // low 32 bits

    // Combine them to form an unsigned long (in terms of byte representation)
    long unsignedLong = (high & 0xFFFFFFFFL) << 32 | (low & 0xFFFFFFFFL);
    return unsignedLong;
  }

}
