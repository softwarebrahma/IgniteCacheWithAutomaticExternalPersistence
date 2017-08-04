package com.brocade.dcm.server.service;

import java.nio.ByteBuffer;
import java.util.UUID;

import org.apache.ignite.lang.IgniteUuid;

import java.util.Base64;

public class UUIDUtils {

  public static void main(String[] ar) {
	  UUID uuid = UUID.randomUUID();
	  System.out.println("UUID string is\n" + uuid);
	  String compressUUID = compress(uuid);
	  System.out.println("Compressed UUID string is\n" + compressUUID);
	  UUID uuid2 = decompress(compressUUID);
	  System.out.println("Decompressed UUID string is\n" + uuid2);
	  System.out.println("Check if both UUIDs are same True/False : " + uuid.equals(uuid2));
  }
  
  public static String compress(UUID uuid) {
    ByteBuffer bb = ByteBuffer.allocate(Long.BYTES * 2);
    bb.putLong(uuid.getMostSignificantBits());
    bb.putLong(uuid.getLeastSignificantBits());
    byte[] array = bb.array();
    return Base64.getUrlEncoder().encodeToString(array);
  }

  public static UUID decompress(String compressUUID) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(Base64.getUrlDecoder().decode(compressUUID));
    return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
  }
  
  public static String compressIgniteUUID(IgniteUuid uuid) {
	    ByteBuffer bb = ByteBuffer.allocate(Long.BYTES * 3);
	    bb.putLong(uuid.globalId().getMostSignificantBits());
	    bb.putLong(uuid.globalId().getLeastSignificantBits());
	    bb.putLong(uuid.localId());
	    byte[] array = bb.array();
	    return Base64.getUrlEncoder().encodeToString(array);
  }
  
  public static IgniteUuid decompressIgniteUUID(String compressedUUID) {
	    ByteBuffer byteBuffer = ByteBuffer.wrap(Base64.getUrlDecoder().decode(compressedUUID));
	    return new IgniteUuid(new UUID(byteBuffer.getLong(), byteBuffer.getLong()), byteBuffer.getLong());
  }

}