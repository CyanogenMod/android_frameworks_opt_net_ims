/*******************************************************************************
 * Software Name : RCS IMS Stack
 *
 * Copyright (C) 2010 France Telecom S.A.
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
 ******************************************************************************/
package org.gsma.joyn;

//import com.orangelabs.rcs.platform.AndroidFactory;

import org.gsma.joyn.chat.ChatLog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * joyn Service configuration
 *
 * @author Jean-Marc AUFFRET
 */
public class JoynServiceConfiguration {
    /**
     * Returns True if the joyn service is activated, else returns False. The
     * service may be activated or deactivated by the end user via the joyn
     * settings application.
     *
     * @param ctx
     *            Context
     * @return Boolean
     */
    
    /**
     * Boolean value "true"
     */
    public static final String TRUE = Boolean.toString(true);

    /**
     * Boolean value "false"
     */
    public static final String FALSE = Boolean.toString(false);

    public static boolean isServiceActivated() {
        /*
         * if
         * (AndroidFactory.getApplicationContext().checkCallingOrSelfPermission
         * (Permissions.READ_RCS_STATE) != PackageManager.PERMISSION_GRANTED) {
         * throw new SecurityException(" Required permission READ_RCS_STATE"); }
         */
        /*
         * boolean result = false; Uri databaseUri =
         * Uri.parse("content://com.orangelabs.rcs.settings/settings");
         * ContentResolver cr =
         * AndroidFactory.getApplicationContext().getContentResolver(); Cursor c
         * = cr.query(databaseUri, null, "key" + "='" + "ServiceActivated" +
         * "'", null, null); if (c != null) { if ((c.getCount() > 0) &&
         * c.moveToFirst()) { String value = c.getString(2); result =
         * Boolean.parseBoolean(value); } c.close(); } return result;
         */
        return false;
    }

    /**
     * Returns True if the joyn service is activated, else returns False. The
     * service may be activated or deactivated by the end user via the joyn
     * settings application.
     *
     * @param ctx
     * @return Boolean
     */
    public static boolean isServiceActivated(Context ctx) {
        boolean result = false;
        Uri databaseUri = Uri
                .parse("content://com.orangelabs.rcs.settings/settings");
        ContentResolver cr = ctx.getContentResolver();
        Cursor c = cr.query(databaseUri, null, "key" + "='"
                + "ServiceActivated" + "'", null, null);
        if (c != null) {
            if ((c.getCount() > 0) && c.moveToFirst()) {
                String value = c.getString(2);
                result = Boolean.parseBoolean(value);
            }
            c.close();
        }
        return result;
    }

    /**
     * Returns the display name associated to the joyn user account. The display
     * name may be updated by the end user via the joyn settings application.
     *
     * @param ctx
     *            Context
     * @return Display name
     */
    public static String getUserDisplayName() {
        // TODO: to be changed
        /*
         * String result = null; Uri databaseUri =
         * Uri.parse("content://com.orangelabs.rcs.settings/settings");
         * ContentResolver cr =
         * AndroidFactory.getApplicationContext().getContentResolver(); Cursor c
         * = cr.query(databaseUri, null, "key" + "='" + "ImsDisplayName" + "'",
         * null, null); if (c != null) { if ((c.getCount() > 0) &&
         * c.moveToFirst()) { result = c.getString(2); } c.close(); } return
         * result;
         */
        return null;
    }

    /**
     * Get the alias name
     *
     * @param contact no
     * @result alias name for contact
     */
    public static String getAliasName(Context ctx, String Contact) {
         Log.d("getAliasName ", Contact);
         Uri CONTENT_URI = Uri.parse("content://com.orangelabs.rcs.chat/message");
         ContentResolver cr = ctx.getContentResolver();
        String aliasName = "";
        Cursor cursor = cr.query(CONTENT_URI,
                new String[] {
                ChatLog.Message.DISPLAY_NAME,
                },
                "(" + ChatLog.Message.CONTACT_NUMBER + "='" + Contact + "' "+
                "AND " +ChatLog.Message.DISPLAY_NAME + " <> '' "+")",
                null,
                ChatLog.GroupChat.TIMESTAMP + " DESC");
        if (cursor.moveToFirst()) {
            String status = cursor.getString(0);
            aliasName = status;
        }
        cursor.close();
        return aliasName;
    }

    /**
     * Profile Auth
     *
     * @param ctx
     * @return Boolean
     */
    public boolean getProfileAuth(Context ctx) {

     // TODO: to be changed
        boolean result = false;
        Uri databaseUri = Uri
                .parse("content://com.orangelabs.rcs.settings/settings");
        ContentResolver cr = ctx.getContentResolver();
        Cursor c = cr.query(databaseUri, null, "key" + "='" + "profileAuth"
                + "'", null, null);
        if (c != null) {
            if ((c.getCount() > 0) && c.moveToFirst()) {
                String value = c.getString(2);
                result = Boolean.parseBoolean(value);
            }
            c.close();
        }
        return result;
    }



    /**
     * NAB Authentication
     *
     * @param ctx
     * @return Auth Type
     */
    public boolean getNABAuth(Context ctx) {
        boolean result = false;
        Uri databaseUri = Uri
                .parse("content://com.orangelabs.rcs.settings/settings");
        ContentResolver cr = ctx.getContentResolver();
        Cursor c = cr.query(databaseUri, null, "key" + "='" + "nabAuth" + "'",
                null, null);
        if (c != null) {
            if ((c.getCount() > 0) && c.moveToFirst()) {
                String value = c.getString(2);
                result = Boolean.parseBoolean(value);
            }
            c.close();
        }
        return result;
    }

    /**
     * Public Account Auth
     *
     * @param ctx
     * @return Boolean Auth Type
     */
     public boolean getPublicAccountAUTH(Context ctx) {
         boolean result = false;
        Uri databaseUri = Uri
                .parse("content://com.orangelabs.rcs.settings/settings");
         ContentResolver cr = ctx.getContentResolver();
        Cursor c = cr.query(databaseUri, null, "key" + "='"
                + "publicAccountAuth" + "'", null, null);
         if (c != null) {
             if ((c.getCount() > 0) && c.moveToFirst()) {
                 String value = c.getString(2);
                 result = Boolean.parseBoolean(value);
             }
             c.close();
         }
         return result;
     }

   /**
    * SSo Auth
    *
     * @param ctx
     * @return Boolean Auth type
    */
    public boolean getSSOAuth(Context ctx) {
        boolean result = false;
        Uri databaseUri = Uri
                .parse("content://com.orangelabs.rcs.settings/settings");
        ContentResolver cr = ctx.getContentResolver();
        Cursor c = cr.query(databaseUri, null, "key" + "='" + "ssoAuth" + "'",
                null, null);
        if (c != null) {
            if ((c.getCount() > 0) && c.moveToFirst()) {
                String value = c.getString(2);
                result = Boolean.parseBoolean(value);
            }
            c.close();
        }
        return result;
    }

   /**
    * Profile Address
    *
     * @param ctx
     * @return String address
    */
    public String getProfileAddress(Context ctx) {
        String result = null;
        Uri databaseUri = Uri
                .parse("content://com.orangelabs.rcs.settings/settings");
        ContentResolver cr = ctx.getContentResolver();
        Cursor c = cr.query(databaseUri, null, "key" + "='" + "profileAddress"
                + "'", null, null);
        if (c != null) {
            if ((c.getCount() > 0) && c.moveToFirst()) {
                result = c.getString(2);
            }
            c.close();
        }
        return result;
    }


   /**
    * Profile Port
    *
     * @param ctx
     * @return String port
    */
    public String getProfileAddressPort(Context ctx) {
        String result = null;
        Uri databaseUri = Uri
                .parse("content://com.orangelabs.rcs.settings/settings");
        ContentResolver cr = ctx.getContentResolver();
        Cursor c = cr.query(databaseUri, null, "key" + "='"
                + "profileAddressPort" + "'", null, null);
        if (c != null) {
            if ((c.getCount() > 0) && c.moveToFirst()) {
                result = c.getString(2);
            }
            c.close();
        }
        return result;
    }

    /**
     * Profile Address Type
     *
     * @param ctx
     * @return String Address
     */
     public String getProfileAddressType(Context ctx) {
         String result = null;
        Uri databaseUri = Uri
                .parse("content://com.orangelabs.rcs.settings/settings");
         ContentResolver cr = ctx.getContentResolver();
        Cursor c = cr.query(databaseUri, null, "key" + "='"
                + "ProfileAddressType" + "'", null, null);
         if (c != null) {
             if ((c.getCount() > 0) && c.moveToFirst()) {
                 result = c.getString(2);
             }
             c.close();
         }
         return result;
     }


     /**
      * Profile Address
      *
     * @param ctx
     * @return String address
      */
      public String getNABAddress(Context ctx) {
          String result = null;
        Uri databaseUri = Uri
                .parse("content://com.orangelabs.rcs.settings/settings");
          ContentResolver cr = ctx.getContentResolver();
        Cursor c = cr.query(databaseUri, null, "key" + "='" + "nabAddress"
                + "'", null, null);
          if (c != null) {
              if ((c.getCount() > 0) && c.moveToFirst()) {
                  result = c.getString(2);
              }
              c.close();
          }
          return result;
      }


     /**
     * NAB Address Port
      *
     * @param ctx
     * @return String port
      */
      public String getNABAddressPort(Context ctx) {
          String result = null;
        Uri databaseUri = Uri
                .parse("content://com.orangelabs.rcs.settings/settings");
          ContentResolver cr = ctx.getContentResolver();
        Cursor c = cr.query(databaseUri, null, "key" + "='" + "nabAddressPort"
                + "'", null, null);
          if (c != null) {
              if ((c.getCount() > 0) && c.moveToFirst()) {
                  result = c.getString(2);
              }
              c.close();
          }
          return result;
      }

     /**
     * NAB Address Type
      *
     * @param ctx
     * @return String Address
      */
      public String getNABAddressType(Context ctx) {
          String result = null;
        Uri databaseUri = Uri
                .parse("content://com.orangelabs.rcs.settings/settings");
          ContentResolver cr = ctx.getContentResolver();
        Cursor c = cr.query(databaseUri, null, "key" + "='" + "nabAddressType"
                + "'", null, null);
          if (c != null) {
              if ((c.getCount() > 0) && c.moveToFirst()) {
                  result = c.getString(2);
              }
              c.close();
          }
          return result;
      }

     /**
      * Public Address
      *
     * @param ctx
     * @return String address
      */
      public String getPublicAccountAddress(Context ctx) {
          String result = null;
        Uri databaseUri = Uri
                .parse("content://com.orangelabs.rcs.settings/settings");
          ContentResolver cr = ctx.getContentResolver();
        Cursor c = cr.query(databaseUri, null, "key" + "='"
                + "publicAccountAddress" + "'", null, null);
          if (c != null) {
              if ((c.getCount() > 0) && c.moveToFirst()) {
                  result = c.getString(2);
              }
              c.close();
          }
          return result;
      }


     /**
     * Public Account Port
      *
     * @param ctx
     * @return String port
      */
      public String getPublicAccountAddressPort(Context ctx) {
          String result = null;
        Uri databaseUri = Uri
                .parse("content://com.orangelabs.rcs.settings/settings");
          ContentResolver cr = ctx.getContentResolver();
        Cursor c = cr.query(databaseUri, null, "key" + "='"
                + "publicAccountAddressPort" + "'", null, null);
          if (c != null) {
              if ((c.getCount() > 0) && c.moveToFirst()) {
                  result = c.getString(2);
              }
              c.close();
          }
          return result;
      }

     /**
     * Public Account Address Type
      *
     * @param ctx
     * @return String Address
      */
      public String getPublicAccountAddressType(Context ctx) {
          String result = null;
        Uri databaseUri = Uri
                .parse("content://com.orangelabs.rcs.settings/settings");
          ContentResolver cr = ctx.getContentResolver();
        Cursor c = cr.query(databaseUri, null, "key" + "='"
                + "publicAccountAddressType" + "'", null, null);
          if (c != null) {
              if ((c.getCount() > 0) && c.moveToFirst()) {
                  result = c.getString(2);
              }
              c.close();
          }
          return result;
      }

     /**
      * SSo Address
      *
     * @param ctx
      * @return address
      */
      public String getSSOAddress(Context ctx) {
          String result = null;
        Uri databaseUri = Uri
                .parse("content://com.orangelabs.rcs.settings/settings");
          ContentResolver cr = ctx.getContentResolver();
        Cursor c = cr.query(databaseUri, null, "key" + "='" + "SSOAddress"
                + "'", null, null);
          if (c != null) {
              if ((c.getCount() > 0) && c.moveToFirst()) {
                  result = c.getString(2);
              }
              c.close();
          }
          return result;
      }


    /**
     * SSO Port
     *
     * @param ctx
     * @return port
     */
      public String getSSOAddressPort(Context ctx) {
          String result = null;
        Uri databaseUri = Uri
                .parse("content://com.orangelabs.rcs.settings/settings");
          ContentResolver cr = ctx.getContentResolver();
        Cursor c = cr.query(databaseUri, null, "key" + "='" + "SSOAddressPort"
                + "'", null, null);
          if (c != null) {
              if ((c.getCount() > 0) && c.moveToFirst()) {
                  result = c.getString(2);
              }
              c.close();
          }
          return result;
      }

    /**
     * Address Type
     *
     * @param ctx
     * @return Address
     */
     public String getSSOAddressType(Context ctx) {
         String result = null;
        Uri databaseUri = Uri
                .parse("content://com.orangelabs.rcs.settings/settings");
         ContentResolver cr = ctx.getContentResolver();
        Cursor c = cr.query(databaseUri, null, "key" + "='" + "SSOAddressType"
                + "'", null, null);
         if (c != null) {
             if ((c.getCount() > 0) && c.moveToFirst()) {
                 result = c.getString(2);
             }
             c.close();
         }
         return result;
     }

    /**
     * Public Uri set by stack
     *
     * @param ctx
     * @return String
     */
     public String getPublicUri(Context ctx) {
         String result = "";
         Uri databaseUri = Uri.parse("content://com.orangelabs.rcs.settings/settings");
         ContentResolver cr = ctx.getContentResolver();
         Cursor c = cr.query(databaseUri, null,
                 "key" + "='" + "publicUri" + "'", null, null);
         if (c != null) {
             if ((c.getCount() > 0) && c.moveToFirst()) {
                 result = c.getString(2);
             }
             c.close();
         }

         return result;
     }

     /**
      * Configuration
      * 
     * @param ctx
      * @return Configuration State
      */
      public boolean getConfigurationState(Context ctx) {
          boolean result = false;
        Uri databaseUri = Uri
                .parse("content://com.orangelabs.rcs.settings/settings");
          ContentResolver cr = ctx.getContentResolver();
        Cursor c = cr.query(databaseUri, null, "key" + "='"
                + "configurationState" + "'", null, null);
          if (c != null) {
              if ((c.getCount() > 0) && c.moveToFirst()) {
                  String value = c.getString(2);
                  result = Boolean.parseBoolean(value);
              }
              c.close();
          }
          return result;
      }



      /**
       * Service Activation State
       *
     * @param ctx
     * @return String Service State
       */
       public static boolean getServiceState(Context ctx) {
           boolean result = false;
        Uri databaseUri = Uri
                .parse("content://com.orangelabs.rcs.settings/settings");
           ContentResolver cr = ctx.getContentResolver();
        Cursor c = cr.query(databaseUri, null, "key" + "='"
                + "ServiceActivated" + "'", null, null);
           if (c != null) {
               if ((c.getCount() > 0) && c.moveToFirst()) {
                   String value = c.getString(2);
                   result = Boolean.parseBoolean(value);
               }
               c.close();
           }
           return result;
       }

       /**
        * Set the root directory for files
        *
        *  @param path Directory path
        */
       public void setFileRootDirectory(String path, Context ctx) {
           Uri databaseUri = Uri
           .parse("content://com.orangelabs.rcs.settings/settings");
           if (ctx != null) {
               ContentResolver cr = ctx.getContentResolver();
               ContentValues values = new ContentValues();
               values.put("value", path);
               String where = "key" + "='" + "DirectoryPathFiles" + "'";
              // long startTime = System.currentTimeMillis();
               cr.update(databaseUri, values, where, null);
           }
       }

       /** Set the root directory for Photo
       *
       *  @param path Directory path
       */
      public void setPhotoRootDirectory(String path, Context ctx) {
          Uri databaseUri = Uri
          .parse("content://com.orangelabs.rcs.settings/settings");
          if (ctx != null) {
              ContentResolver cr = ctx.getContentResolver();
              ContentValues values = new ContentValues();
              values.put("value", path);
              String where = "key" + "='" + "DirectoryPathPhotos" + "'";
             // long startTime = System.currentTimeMillis();
              cr.update(databaseUri, values, where, null);
          }
      }

      /** Set the root directory for Videos
       *
       *  @param path Directory path
       */
      public void setVideoRootDirectory(String path, Context ctx) {
          Uri databaseUri = Uri
          .parse("content://com.orangelabs.rcs.settings/settings");
          if (ctx != null) {
              ContentResolver cr = ctx.getContentResolver();
              ContentValues values = new ContentValues();
              values.put("value", path);
              String where = "key" + "='" + "DirectoryPathVideos" + "'";
             // long startTime = System.currentTimeMillis();
              cr.update(databaseUri, values, where, null);
          }
      }
      
      /** Set the root directory for Photo
      *
      *  @param path Directory path
      */
      public static void setServicePermissionState(boolean state, Context ctx) {
          String stringState = "";
          if(state) {
              stringState = TRUE;
          } else {
              stringState = FALSE;
          }
          Uri databaseUri = Uri
          .parse("content://com.orangelabs.rcs.settings/settings");
          if (ctx != null) {
          ContentResolver cr = ctx.getContentResolver();
          ContentValues values = new ContentValues();
              values.put("value", stringState);
              String where = "key" + "='" + "servicePermitted" + "'";
              // long startTime = System.currentTimeMillis();
              cr.update(databaseUri, values, where, null);
          }
      }
      
      /**
       * Returns True if the App Permission is granted .
       *
       * @param ctx
       * @return Boolean
       */
      public static boolean isServicePermission(Context ctx) {
          boolean result = false;
          Uri databaseUri = Uri
                  .parse("content://com.orangelabs.rcs.settings/settings");
          ContentResolver cr = ctx.getContentResolver();
          Cursor c = cr.query(databaseUri, null, "key" + "='"
                  + "servicePermitted" + "'", null, null);
          if (c != null) {
              if ((c.getCount() > 0) && c.moveToFirst()) {
                  String value = c.getString(2);
                  result = Boolean.parseBoolean(value);
              }
              c.close();
          }
          return result;
      }
}
