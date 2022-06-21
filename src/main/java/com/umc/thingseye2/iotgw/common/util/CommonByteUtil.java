package com.umc.thingseye2.iotgw.common.util;

/**
 * byte parsing 처리를 위한 util.
 * @author : JiHwanKang
 * @since : 2019. 3. 14.
 */
public class CommonByteUtil {
	public static final String LITTLE_ENDIAN = "LE";
	public static final String BIG_ENDIAN = "BE";
    
	/**
	 * byte를 short으로 parsing.
	 * @param b1
	 * @param b0
	 * @return : short.
	 */
    static private short makeShort(byte b1, byte b0) {
        return (short)((b1 << 8) | (b0 & 0xff));
    }

    /**
     * LE를 통해 2byte 처리.
     * @param bb
     * @param bi
     * @return : short.
     */
    static private short getShortL(byte[] bb, int bi) {
    	return makeShort(bb[bi+1],
    			         bb[bi  ]);
    }

    /**
     * BE를 통해 2byte 처리.
     * @param bb
     * @param bi
     * @return : short.
     */
    static private short getShortB(byte[] bb, int bi) {
        return makeShort(bb[bi  ],
                         bb[bi+1]);
    }

    /**
     * LE/BE에 따라 short parsing을 위한 분기 처리.
     * @param bb
     * @param bi
     * @param align
     * @return : short.
     */
    static public short getShort(byte[] bb, int bi, String align) {
        return align.equals(LITTLE_ENDIAN) ? getShortL(bb, bi) : getShortB(bb, bi);
    }

    /**
     * short를 byte로 parsing 처리.
     * @param x
     * @return :
     */
    private static byte short1(short x) { return (byte)(x >> 8); }
    private static byte short0(short x) { return (byte)(x     ); }
    
    static private void putShortL(byte[] bb, int bi, short x) {
        bb[bi  ] = short0(x);
        bb[bi+1] = short1(x);
    }

    static private void putShortB(byte[] bb, int bi, short x) {
        bb[bi  ] = short1(x);
        bb[bi+1] = short0(x);
    }
    
    /**
     * 
     * byte[0]: 첫번째 비트 부호, 뒤에 7비트 숫자 .
     * byte[1]: 소수자리.
     * @param bb
     * @param index
     * @param align
     * @return :
     */
    static public double getSingedShort(byte[] bb, int index, String align) {
    	if(align.equals(LITTLE_ENDIAN)) {
    		return 0;
    	} else {
    		double num = (int)bb[index];
    		double sosu = (int)bb[index+1];
    		if(sosu > 0) {
    			int len = (int)(Math.log10(sosu)+1);
    			sosu = sosu / (10*len);
    		}
    		return num + sosu;
    		
    	}
    }

    static public void putShort(byte[] bb, int bi, short x, String align) {
        if (align.equals(LITTLE_ENDIAN))
        	putShortL(bb, bi, x);
        else
        	putShortB(bb, bi, x);
    }
    
    static public byte[] shortToByteArray(short x, String align) {
    	byte[] array = new byte[2];
    			
    	if (align.equals(LITTLE_ENDIAN)){
    		array[0] = short0(x);
    		array[1] = short1(x);
    	}else{
    		array[0] = short1(x);
    		array[1] = short0(x);
    	}
    	return array;
    }
    
    static public String shortToByteArrayString(short x, String align) {
    	byte[] array = shortToByteArray(x, align);
    	return byteArrayToString(array);
    }
    
    static public String ushortToByteArrayString(int x, String align) {
    	byte[] array = new byte[4];
    	if (align.equals(LITTLE_ENDIAN)){
    		putIntL(array, 0, x);
    	}else{
    		putIntB(array, 0, x);
    	}
    	byte[] temp = new byte[2];
    	System.arraycopy(array, 2, temp, 0, 2);
    	return byteArrayToString(temp);
    }
    
    static String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
    static public String byteArrayToString(byte data) {
    	StringBuilder sb = new StringBuilder();
    	int	src = (int)data;
    	int	left =  ((src & 0xf0) >> 4);
    	int	right = src & 0x0f;
    		
		sb.append( hex[left] );
		sb.append( hex[right] );
    	return sb.toString();
    }
    static public String byteArrayToString(byte[] data) {
    	StringBuilder sb = new StringBuilder();
    	int length = data.length;
    	int src, left, right;
    	for ( int i = 0; i < length; i++) {
			src = (int)data[i];
			left =  ((src & 0xf0) >> 4);
			right = src & 0x0f;

			sb.append( hex[left] );
			sb.append( hex[right] );
		}
    	return sb.toString();
    }

    static public int getUShort(byte[] bb, int index, String align) {      	    	
		return align.equals(LITTLE_ENDIAN) ? getUShortL(bb, index) : getUShortB(bb, index) ;
	}
    
    static public int getUShortL(byte[] bb, int index) {
    	return makeInt((byte)0x00,
    			   (byte)0x00,
	 			   bb[index+1],
	 			   bb[index]);
    }

    static public int getUShortB(byte[] bb, int index) {
    	return makeInt((byte)0x00,
  			   (byte)0x00,
  			   bb[index],
 			   bb[index+1]
 			   );
    }
    
    static public void putUShort(byte[] bb, int index, int x, String align) {
    	if(align.equals(LITTLE_ENDIAN))
    		putUShortL(bb, index, x);
    	else
    		putUShortB(bb, index, x);
	}
    
    static private void putUShortL(byte[] bb, int index, int x) {
        bb[index+1] = (byte)(x >> 8);
        bb[index  ] = (byte)(x     );
    }

    static private void putUShortB(byte[] bb, int index, int x) {
    	bb[index  ] = (byte)(x >> 8);
        bb[index+1] = (byte)(x 	   );
    }
    
	// int get/put
    static public String intToByteArrayString(int x, String align) {
    	byte[] array = new byte[4];
    	if (align.equals(LITTLE_ENDIAN)){
    		putIntL(array, 0, x);
    	}else{
    		putIntB(array, 0, x);
    	}
    	
    	return byteArrayToString(array);
    }
    
    static public String intToByteArrayString2(int x, String align) {
    	byte[] array = new byte[2];
    	if (align.equals(LITTLE_ENDIAN)){
    		putIntL(array, 0, x);
    	}else{
    		putIntB(array, 0, x);
    	}
    	
    	return byteArrayToString(array);
    }
    
    // String get/put
    static public String StringToByteArrayString(String x) {
    	byte[] array = new byte[4];
    	array = x.getBytes();
    	return byteArrayToString(array);
    }
    
    static public byte[] intToByteArray(int x, String align) {
    	byte[] array = new byte[4];
    	if (align.equals(LITTLE_ENDIAN)){
    		putIntL(array, 0, x);
    	}else{
    		putIntB(array, 0, x);
    	}
    	
    	return array;
    }
    
    static public int getUnsignedByte(byte bb) {
		int unSignedByte = bb & 0xFF;
    	return unSignedByte;
	}
    
    //TODO : comment 달 것.
	static public int getInt(byte[] bb, int index, String align) {
		return align.equals(LITTLE_ENDIAN) ? getIntL(bb, index) : getIntB(bb, index) ;
	}

    static public int getIntL(byte[] bb, int index) {
    	return makeInt(bb[index+3],
	 			   bb[index+2],
	 			   bb[index+1],
	 			   bb[index]);
    }

    static public int getIntB(byte[] bb, int index) {
    	return makeInt(bb[index],
 			   bb[index+1],
 			   bb[index+2],
 			   bb[index+3]);
    }
	
    static private int makeInt(byte b3, byte b2, byte b1, byte b0) {
    	return (((b3       ) << 24) |
    			((b2 & 0xff) << 16) |
    			((b1 & 0xff) <<  8) |
    			((b0 & 0xff)      ));
    }
    
    static public void putInt(byte[] bb, int index, int x, String align) {
    	if(align.equals(LITTLE_ENDIAN))
    		putIntL(bb, index, x);
    	else
    		putIntB(bb, index, x);
	}
    
    static private void putIntL(byte[] bb, int index, int x) {
        bb[index+3] = (byte)(x >> 24);
        bb[index+2] = (byte)(x >> 16);
        bb[index+1] = (byte)(x >> 8);
        bb[index  ] = (byte)(x     );
    }

    static private void putIntB(byte[] bb, int index, int x) {
    	bb[index  ] = (byte)(x >> 24);
        bb[index+1] = (byte)(x >> 16);
        bb[index+2] = (byte)(x >> 8);
        bb[index+3] = (byte)(x     );
    }
    
 // -- get/put long --

    static private long makeLong(byte b7, byte b6, byte b5, byte b4,
                                 byte b3, byte b2, byte b1, byte b0)
    {
        return ((((long)b7       ) << 56) |
                (((long)b6 & 0xff) << 48) |
                (((long)b5 & 0xff) << 40) |
                (((long)b4 & 0xff) << 32) |
                (((long)b3 & 0xff) << 24) |
                (((long)b2 & 0xff) << 16) |
                (((long)b1 & 0xff) <<  8) |
                (((long)b0 & 0xff)      ));
    }

    static private long getLongL(byte[] bb, int bi) {
        return makeLong(bb[bi + 7],
                        bb[bi + 6],
                        bb[bi + 5],
                        bb[bi + 4],
                        bb[bi + 3],
                        bb[bi + 2],
                        bb[bi + 1],
                        bb[bi    ]);
    }

    static private long getLongB(byte[] bb, int bi) {
        return makeLong(bb[bi    ],
                        bb[bi + 1],
                        bb[bi + 2],
                        bb[bi + 3],
                        bb[bi + 4],
                        bb[bi + 5],
                        bb[bi + 6],
                        bb[bi + 7]);
    }

    static public long getLong(byte[] bb, int bi, String align) {
        return align.equals(BIG_ENDIAN) ? getLongB(bb, bi) : getLongL(bb, bi);
    }

    private static byte long7(long x) { return (byte)(x >> 56); }
    private static byte long6(long x) { return (byte)(x >> 48); }
    private static byte long5(long x) { return (byte)(x >> 40); }
    private static byte long4(long x) { return (byte)(x >> 32); }
    private static byte long3(long x) { return (byte)(x >> 24); }
    private static byte long2(long x) { return (byte)(x >> 16); }
    private static byte long1(long x) { return (byte)(x >>  8); }
    private static byte long0(long x) { return (byte)(x      ); }
    public static final int getInt1byte(byte i) { return ((0x00 << 8) | (i & 0xff)); }
    
    static private void putLongL(byte[] bb, int bi, long x) {
        bb[bi + 7] = long7(x);
        bb[bi + 6] = long6(x);
        bb[bi + 5] = long5(x);
        bb[bi + 4] = long4(x);
        bb[bi + 3] = long3(x);
        bb[bi + 2] = long2(x);
        bb[bi + 1] = long1(x);
        bb[bi    ] = long0(x);
    }
    
    static private void putLongB(byte[] bb, int bi, long x) {
        bb[bi    ] = long7(x);
        bb[bi + 1] = long6(x);
        bb[bi + 2] = long5(x);
        bb[bi + 3] = long4(x);
        bb[bi + 4] = long3(x);
        bb[bi + 5] = long2(x);
        bb[bi + 6] = long1(x);
        bb[bi + 7] = long0(x);
    }

    static public void putLong(byte[] bb, int bi, long x, String align) {
        if (align.equals(BIG_ENDIAN))
            putLongB(bb, bi, x);
        else
            putLongL(bb, bi, x);
    }
    
 // hex to byte[]
    public static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() == 0) {
            return null;
        }
     
        byte[] ba = new byte[hex.length() / 2];
        for (int i = 0; i < ba.length; i++) {
            ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return ba;
    }
     
    // byte[] to hex
    public static String byteArrayToHex(byte[] ba) {
        if (ba == null || ba.length == 0) {
            return null;
        }
     
        StringBuffer sb = new StringBuffer(ba.length * 2);
        String hexNumber;
        for (int x = 0; x < ba.length; x++) {
            hexNumber = "0" + Integer.toHexString(0xff & ba[x]);
     
            sb.append(hexNumber.substring(hexNumber.length() - 2));
        }
        return sb.toString();
    } 
    
	public static int byteToInt(byte b) {
		return b & 0xff;
	}

}
