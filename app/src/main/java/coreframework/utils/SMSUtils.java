package coreframework.utils;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Date;

import android.content.Context;

/**
 * @author blrlc332|Mohit
 */
public class SMSUtils {

    public static byte[] hash = null;
    /**
     * @info Byte Array Object
     */
    public static final byte cw_bArray = 0x01;

    /**
     * @info String Object
     */
    public static final byte cw_string = 0x02;
    /**
     * @info String containg hex code Object
     */
    public static final byte cw_hex_string = 0x03;
    /**
     * @info Short Object
     */
    public static final byte cw_short = 0x04;
    /**
     * @info Pre format LV Byte Array Object - No encoding will be performed
     */
    public static final byte cw_pre_formed = 0x00;
    public static final byte cw_date_long = 0x08;

    public static final byte cw_Integer_obj = 0x09;// encoding of encapsulated
    // of Integer Object/Can be
    // null as well
    public static final byte cw_Short_obj = 0x10; // encoding of encapsulated
    // Short Obkect / Can be
    // null as well
    public static final byte cw_Float_obj = 0x11;// encoding of encapsulated
    // Float Object / Can be
    // null as well
    public static final byte cw_BYTE_obj = 0x12;
    // BYTE object / can be null
    // as well but ideally such
    // case should not
    // happening in the design label;;

    /**
     * @param buffer
     * @param offset
     * @param codeWord
     * @return Object Array Containg all the values from the LV formatted input
     * data
     */
    public static Object[] unMountLVParams(byte[] buffer, int offset,
                                           byte[] codeWord) {
        try {
            Object obj[] = null;// Initial Loading Capaicty Initialization
            int comp_cnt = 0;
            byte[] data = null;
            int lengthOfParam = -1;
            if (codeWord == null) {
                obj = new Object[10];// Initial Loading Capacity
            } else {
                obj = new Object[codeWord.length];
            }
            while (offset < buffer.length) {
                lengthOfParam = buffer[offset] & 0x000000ff;
                if (buffer[offset] == (byte) 0xff) {
                    int actDataLength = (int) ((buffer[offset + 1] << 8 & 0xff00) | (buffer[offset + 2] & 0x00ff));
                    offset += 2;
                    lengthOfParam = actDataLength;
                }
                data = new byte[lengthOfParam];
                offset++;
                System.arraycopy(buffer, offset, data, 0, lengthOfParam);
                obj[comp_cnt] = (codeWord == null) ? data
                        : loadObjectTypeFromCodeWord(data, codeWord[comp_cnt]);
                comp_cnt++;
                offset += lengthOfParam;
            }
            if (codeWord == null) {
                // re-write the object buffer with the specified length
                Object[] obj2 = new Object[comp_cnt];
                System.arraycopy(obj, 0, obj2, 0, comp_cnt);
                // obj = null;//for gc mem pd
                obj = obj2;
            }
            if (codeWord != null) {
                if (codeWord.length - comp_cnt > 0) {
                    // The code word size is greater than the number of
                    // components actually read
                    Object[] obj2 = new Object[comp_cnt];
                    System.arraycopy(obj, 0, obj2, 0, comp_cnt);
                    // obj = null;//for gc mem pd
                    obj = obj2;
                }
            }
            // if(obj.length != codeWord.length){
            // return null;//XXX: Ambigous statement.needed to be checked...
            // }
            return obj;

        } catch (Exception e) {
            //Log.e("Unmount Error=", "Unmount Error=" + e);
            return null;
        }
    }

    public static Object loadObjectTypeFromCodeWord_(byte[] data, byte codeWord) {
        switch (codeWord) {
            case cw_bArray:// byteArray
                return data;
            case cw_string:// String Type
                return new String(data);
            case cw_hex_string:// Hex Object String
                return Hex.toHex(data);
            case cw_BYTE_obj:
                return data.length != 0 ? new Byte(data[0]) : null;

            case cw_Integer_obj:
                return data.length != 0 ? new Integer(Hex.byteArrayToInt(data))
                        : null;
            case cw_short:// short type
                if (data.length != 2) {
                    return null;
                }
                return new short[]{(short) ((data[0] << 8 & 0xff00) | (data[1] & 0x00ff))};
            case cw_Short_obj:
                return data.length != 0 ? new Short(Hex.byteArrayToShort(data))
                        : null;

            case cw_Float_obj:
                return data.length != 0 ? new Float(Hex.byteArrayToFloat(data))
                        : null;
            case 0x08: // for TxnDate
                // return RMSUtils.getDisplayableDate(new
                // Date(Hex.byteArrayToLong(data)));
                // XXX:
                return Hex.byteArrayToLong(data);
            default:
                return data;
        }
    }

    public static Object loadObjectTypeFromCodeWord(byte[] data, byte codeWord) {
        switch (codeWord) {
            case cw_bArray:// byteArray
                return data;
            case cw_string:// String Type
                return new String(data);
            case cw_hex_string:// Hex Object String
                return Hex.toHex(data);
            case cw_short:// short type
                if (data.length != 2) {
                    return null;
                }
                return new short[]{(short) ((data[0] << 8 & 0xff00) | (data[1] & 0x00ff))};
            case 0x08: // for TxnDate
                return DateTimeUtils.getDisplayableDate(new Date(Hex
                        .byteArrayToLong(data)));
            case cw_BYTE_obj:
                return data.length != 0 ? new Byte(data[0]) : null;
            case cw_Short_obj:
                return data.length != 0 ? new Short(Hex.byteArrayToShort(data))
                        : null;
            case cw_Integer_obj:
                return data.length != 0 ? new Integer(Hex.byteArrayToInt(data))
                        : null;
            case cw_Float_obj:
                return data.length != 0 ? new Float(Hex.byteArrayToFloat(data))
                        : null;
            default:
                return data;
        }
    }

    // code word value 0x01 = byte[]
    // code word value 0x02 = String Object
    // code word value 0x03 = Hex String
    // code word value 0x04 = Short
    // code word value 0x00 = pre-formatted byte array lv structure/no encoding
    // is required

    /**
     *
     */
    public static byte[] mountLVParams(byte[] initData, Object[] obj,
                                       byte[] codeWord) {
        byte[] temp;
        try {
            byte[] buffer = null;
            if (codeWord == null) {
                codeWord = new byte[obj.length];
                for (int i = 0; i < codeWord.length; i++) {
                    codeWord[i] = cw_bArray;
                }
            }
            // Initial Capacity Loading
            if (codeWord.length - obj.length != 0) {
                return null;// operation not allowed
            }
            int lengthOfBuffer = 0;
            int component_length = 0;
            for (int i = 0; i < codeWord.length; i++) {
                switch (codeWord[i]) {
                    case cw_bArray:
                        // FIXME: if byte [] which is an object is null associate
                        // with zero length bArray
                        // Mohit 21/10/2011
                        if (obj[i] == null) {
                            obj[i] = new byte[0];
                        }
                        component_length = ((byte[]) obj[i]).length;// length of the
                        // object, can
                        // be zero
                        // length as
                        // well
                        lengthOfBuffer += component_length;
                        lengthOfBuffer += (component_length < 255) ? 1 : 3;// add
                        // the
                        // length
                        // byte/mandatory
                        // 1 or
                        // 2
                        break;
                    case cw_BYTE_obj:
                        lengthOfBuffer += (obj[i] == null) ? 1 : 2;
                        break;
                    case cw_string:
                        // FIXME: if String Object which is an object is null
                        // associate with zero length String Object
                        // Mohit 03/11/2011
                        if (obj[i] == null) {
                            obj[i] = new String();
                        }
                        component_length = ((String) obj[i]).getBytes().length;// length
                        // of
                        // the
                        // object,
                        // can
                        // be
                        // zero
                        // length
                        // as
                        // well
                        lengthOfBuffer += component_length;
                        lengthOfBuffer += (component_length < 255) ? 1 : 3;// add
                        // the
                        // length
                        // byte/mandatory
                        // 1 or
                        // 2
                        break;
                    case cw_hex_string:
                        component_length = Hex.toByteArr((String) obj[i]).length;
                        lengthOfBuffer += component_length;
                        lengthOfBuffer += (component_length < 255) ? 1 : 3;// add
                        // the
                        // length
                        // byte/mandatory
                        // 1 or
                        // 2
                        break;
                    case cw_short:
                        lengthOfBuffer += 3;// 2+1 //Component length params f() not
                        // required/short is always two bytes
                        // so total is 2+1 2(short)+1(length)(byte)
                        break;
                    case cw_Short_obj:// encapsulated could be null as well
                        lengthOfBuffer += (obj[i] == null) ? 1 : 3;
                        break;
                    case cw_Integer_obj: // encapsulated could be null as well
                        lengthOfBuffer += (obj[i] == null) ? 1 : 5;
                        break;
                    case cw_Float_obj: // //encapsulated could be null as well
                        lengthOfBuffer += (obj[i] == null) ? 1 : 5;
                        break;
                    case cw_pre_formed:
                        // Pre-Formatted Byte Array [] with Length
                        // This is already a preformatted bArray with length encoded
                        // as per the rules requirement!
                        // copy the entire object in this case: //Mohit
                        lengthOfBuffer += ((byte[]) obj[i]).length;
                        break;
                    default:
                        return null;
                }
            }
            buffer = new byte[lengthOfBuffer];
            int index = 0;
            int lengthOfComponent = -1;
            for (int i = 0; i < codeWord.length; i++) {
                switch (codeWord[i]) {
                    case cw_bArray:// byte[]
                        lengthOfComponent = ((byte[]) obj[i]).length;
                        if (lengthOfComponent >= 255) {
                            buffer[index] = (byte) 0xff; // denoter of extended
                            // length (2bytes
                            // Encoding)
                            index++;
                            buffer[index] = (byte) ((lengthOfComponent & 0xff00) >>> 8);
                            index++;
                            buffer[index] = (byte) ((lengthOfComponent & 0x00ff));
                            index++;
                        } else {
                            buffer[index] = (byte) lengthOfComponent;
                            index++;
                        }
                        System.arraycopy((byte[]) obj[i], 0, buffer, index,
                                lengthOfComponent);
                        index += lengthOfComponent;
                        break;
                    case cw_BYTE_obj:
                        if (obj[i] == null) {
                            buffer[index] = 0x00;
                            index++;
                        } else {
                            buffer[index] = 0x01;
                            index++;
                            buffer[index] = ((Byte) obj[i]).byteValue();
                            index++;
                        }
                        break;
                    case cw_string:// string
                        lengthOfComponent = ((String) obj[i]).getBytes().length;
                        if (lengthOfComponent >= 255) {
                            buffer[index] = (byte) 0xff; // denoter of extended
                            // length (2bytes
                            // Encoding)
                            index++;
                            buffer[index] = (byte) ((lengthOfComponent & 0xff00) >>> 8);
                            index++;
                            buffer[index] = (byte) ((lengthOfComponent & 0x00ff));
                            index++;
                        } else {
                            buffer[index] = (byte) lengthOfComponent;
                            index++;
                        }
                        System.arraycopy(((String) obj[i]).getBytes(), 0, buffer,
                                index, lengthOfComponent);
                        index += lengthOfComponent;
                        break;
                    case cw_hex_string:// hex string
                        lengthOfComponent = Hex.toByteArr((String) obj[i]).length;
                        if (lengthOfComponent >= 255) {
                            buffer[index] = (byte) 0xff; // denoter of extended
                            // length (2bytes
                            // Encoding)
                            index++;
                            buffer[index] = (byte) ((lengthOfComponent & 0xff00) >>> 8);
                            index++;
                            buffer[index] = (byte) ((lengthOfComponent & 0x00ff));
                            index++;
                        } else {
                            buffer[index] = (byte) lengthOfComponent;
                            index++;
                        }
                        System.arraycopy(Hex.toByteArr((String) obj[i]), 0, buffer,
                                index, lengthOfComponent);
                        index += lengthOfComponent;
                        break;
                    case cw_short:// short
                        lengthOfComponent = 2;
                        buffer[index] = (byte) lengthOfComponent;
                        index++;
                        buffer[index] = (byte) (((short[]) obj[i])[0] >>> 8 & 0x00ff);
                        buffer[index + 1] = (byte) (((short[]) obj[i])[0] & 0x00ff);
                        index += lengthOfComponent;
                        break;
                    case cw_Short_obj:
                        if (obj[i] == null) {
                            buffer[index] = 0x00;
                            index++;
                        } else {
                            buffer[index] = 0x02;
                            index++;
                            buffer[index] = (byte) (((Short) obj[i]).shortValue() >>> 8 & 0x00ff);
                            index++;
                            buffer[index] = (byte) (((Short) obj[i]).shortValue() & 0x00ff);
                            index++;
                        }
                        break;
                    case cw_Integer_obj:
                        if (obj[i] == null) {
                            buffer[index] = 0x00;
                            index++;
                        } else {
                            buffer[index] = 0x04;
                            index++;
                            temp = Hex
                                    .intToByteArray(((Integer) obj[i]).intValue());
                            System.arraycopy(temp, 0, buffer, index, temp.length);
                            index += temp.length;
                        }
                        break;
                    case cw_Float_obj:
                        if (obj[i] == null) {
                            buffer[index] = 0x00;
                            index++;
                        } else {
                            buffer[index] = 0x04;
                            index++;
                            // temp =
                            // Hex.intToByteArray(((Integer)obj[i]).intValue());
                            temp = Hex.floatToByteArray(((Float) obj[i])
                                    .floatValue());
                            System.arraycopy(temp, 0, buffer, index, temp.length);
                            index += temp.length;
                        }
                        break;
                    case cw_pre_formed:// Byte Array With Length Existing
                        lengthOfComponent = ((byte[]) obj[i]).length;
                        System.arraycopy((byte[]) obj[i], 0, buffer, index,
                                lengthOfComponent);
                        index += lengthOfComponent;
                        break;
                    default:
                        return null;
                }
            }
            // copy initial data if any by prepending the initial data to the
            // compiled buffer
            if (initData != null) {
                int newLengthOfBuffer = lengthOfBuffer + initData.length;
                byte[] newBuffer = new byte[newLengthOfBuffer];
                System.arraycopy(initData, 0, newBuffer, 0, initData.length);
                System.arraycopy(buffer, 0, newBuffer, initData.length,
                        lengthOfBuffer);
                buffer = null;
                buffer = newBuffer;
            }
            return buffer;

        } catch (Exception e) {
            //Log.e("ErrorInMounting-->", "ErrorInMounting-->" + e);
            return null;
        }
    }
}
