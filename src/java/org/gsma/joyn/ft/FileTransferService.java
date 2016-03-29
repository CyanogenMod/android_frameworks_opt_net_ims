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
package org.gsma.joyn.ft;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.gsma.joyn.JoynContactFormatException;
import org.gsma.joyn.JoynService;
import org.gsma.joyn.JoynServiceException;
import org.gsma.joyn.JoynServiceListener;
import org.gsma.joyn.JoynServiceNotAvailableException;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import org.gsma.joyn.Logger;

import org.gsma.joyn.ICoreServiceWrapper;

/**
 * This class offers the main entry point to transfer files and to
 * receive files. Several applications may connect/disconnect to the API.
 *
 * The parameter contact in the API supports the following formats:
 * MSISDN in national or international format, SIP address, SIP-URI
 * or Tel-URI.
 *
 * @author Jean-Marc AUFFRET
 */
public class FileTransferService extends JoynService {
    /**
     * API
     */
    private IFileTransferService api = null;

    public static final String TAG = "TAPI-FileTransferService";

    /**
     * Constructor
     *
     * @param ctx Application context
     * @param listener Service listener
     */
    public FileTransferService(Context ctx, JoynServiceListener listener) {
        super(ctx, listener);
    }

    /**
     * Connects to the API
     */
    public void connect() {
        Logger.i(TAG, "FileTransfer connected() entry");
        Intent intent = new Intent();
        ComponentName cmp = new ComponentName("com.orangelabs.rcs", "com.orangelabs.rcs.service.RcsCoreService");
        intent.setComponent(cmp);
        intent.setAction(IFileTransferService.class.getName());

        ctx.bindService(intent, apiConnection, 0);
    }

    /**
     * Disconnects from the API
     */
    public void disconnect() {
        try {
            Logger.i(TAG, "FileTransfer disconnect() entry");
            ctx.unbindService(apiConnection);
        } catch (IllegalArgumentException e) {
            // Nothing to do
        }
    }

    /**
     * Set API interface
     *
     * @param api API interface
     */
    protected void setApi(IInterface api) {
        super.setApi(api);

        this.api = (IFileTransferService) api;
    }

    /**
     * Service connection
     */
    private ServiceConnection apiConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Logger.i(TAG, "onServiceConnected entry " + className);
            ICoreServiceWrapper mCoreServiceWrapperBinder = ICoreServiceWrapper.Stub.asInterface(service);
            IBinder binder = null;
            try {
                binder = mCoreServiceWrapperBinder.getFileTransferServiceBinder();
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
            setApi(IFileTransferService.Stub.asInterface(binder));
            if (serviceListener != null) {
                serviceListener.onServiceConnected();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            Logger.i(TAG, "onServiceDisconnected entry " + className);
            setApi(null);
            if (serviceListener != null) {
                serviceListener.onServiceDisconnected(JoynService.Error.CONNECTION_LOST);
            }
        }
    };

    /**
     * Returns the configuration of the file transfer service
     *
     * @return Configuration
     * @throws JoynServiceException
     */
    public FileTransferServiceConfiguration getConfiguration() throws JoynServiceException {
        Logger.i(TAG, "getConfiguration() entry " + api);
        if (api != null) {
            try {
                return api.getConfiguration();
            } catch (Exception e) {
                throw new JoynServiceException(e.getMessage());
            }
        } else {
            throw new JoynServiceNotAvailableException();
        }
    }

    /**
     * Transfers a file to a contact. The parameter filename contains the complete
     * path of the file to be transferred. The parameter contact supports the following
     * formats: MSISDN in national or international format, SIP address, SIP-URI or
     * Tel-URI. If the format of the contact is not supported an exception is thrown.
     *
     * @param contact
     * @param filename Filename to transfer
     * @param listener File transfer event listener
     * @return File transfer
     * @throws JoynServiceException
     * @throws JoynContactFormatException
     */
    public FileTransfer transferFile(String contact, String filename, FileTransferListener listener) throws JoynServiceException, JoynContactFormatException {
        return transferFile(contact, filename, null, listener);
    }

    /**
     * Transfers a file to a contact. The parameter filename contains the complete
     * path of the file to be transferred. The parameter contact supports the following
     * formats: MSISDN in national or international format, SIP address, SIP-URI or
     * Tel-URI. If the format of the contact is not supported an exception is thrown.
     *
     * @param contact
     * @param filename Filename to transfer
     * @param fileicon Filename of the file icon associated to the file to be transfered
     * @param listener File transfer event listener
     * @return File transfer
     * @throws JoynServiceException
     * @throws JoynContactFormatException
     */
    public FileTransfer transferFile(String contact, String filename, String fileicon, FileTransferListener listener) throws JoynServiceException, JoynContactFormatException {
        /*if (ctx.checkCallingOrSelfPermission(Permissions.RCS_FILETRANSFER_SEND) != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException(" Required permission RCS_FILETRANSFER_SEND");
        }
        */
        Logger.i(TAG, "transferFile() entry contact= " + contact + " filename=" + filename + " fileicon = " + fileicon + " listener=" + listener);
        if (api != null) {
            try {
                IFileTransfer ftIntf = api.transferFile(contact, filename, fileicon, listener);
                if (ftIntf != null) {
                    return new FileTransfer(ftIntf);
                } else {
                    return null;
                }
            } catch (Exception e) {
                throw new JoynServiceException(e.getMessage());
            }
        } else {
            throw new JoynServiceNotAvailableException();
        }
    }

    /**
     * Resumes an interrupted file from the point it got interrupted. Only for MSRP FT
     *
     * @param fileTranferId fileTranferId
     * @param listener File transfer event listener
     * @return File transfer
     * @throws JoynServiceException
     */
    public FileTransfer resumeFileTransfer(String fileTranferId, FileTransferListener listener) throws JoynServiceException
    {
        Logger.i(TAG, "resumeFileTransfer() entry fileTranferId=" + fileTranferId);
        if (api != null) {
            try {
                IFileTransfer ftIntf = api.resumeFileTransfer(fileTranferId, listener);
                if (ftIntf != null) {
                    return new FileTransfer(ftIntf);
                } else {
                    return null;
                }
            } catch (Exception e) {
                throw new JoynServiceException(e.getMessage());
            }
        } else {
            throw new JoynServiceNotAvailableException();
        }
    }

    /**
     * Resumes an interrupted file from the point it got interrupted. Only for MSRP FT
     *
     * @param fileTranferId fileTranferId
     * @param listener File transfer event listener
     * @return File transfer
     * @throws JoynServiceException
     */
    public FileTransfer resumePublicFileTransfer(String fileTranferId, FileTransferListener listener, int timeLen) throws JoynServiceException
    {
        Logger.i(TAG, "resumePublicFileTransfer() entry fileTranferId=" + fileTranferId);
        if (api != null) {
            try {
                IFileTransfer ftIntf = api.resumePublicFileTransfer(fileTranferId, listener, timeLen);
                if (ftIntf != null) {
                    return new FileTransfer(ftIntf);
                } else {
                    return null;
                }
            } catch (Exception e) {
                throw new JoynServiceException(e.getMessage());
            }
        } else {
            throw new JoynServiceNotAvailableException();
        }
    }



    /**
     * Resumes an interrupted file from the point it got interrupted. Only for MSRP FT
     *
     * @param fileTranferId fileTranferId
     * @param listener File transfer event listener
     * @return File transfer
     * @throws JoynServiceException
     */
    public FileTransfer resumeGroupFileTransfer(String chatId, String fileTranferId, FileTransferListener listener) throws JoynServiceException
    {
        Logger.i(TAG, "resumeGroupFileTransfer() entry fileTranferId=" + fileTranferId);
        if (api != null) {
            try {
                IFileTransfer ftIntf = api.resumeGroupFileTransfer(chatId, fileTranferId, listener);
                if (ftIntf != null) {
                    return new FileTransfer(ftIntf);
                } else {
                    return null;
                }
            } catch (Exception e) {
                throw new JoynServiceException(e.getMessage());
            }
        } else {
            throw new JoynServiceNotAvailableException();
        }
    }

    /**
     * Transfers a file to a contact. The parameter filename contains the complete
     * path of the file to be transferred. The parameter contact supports the following
     * formats: MSISDN in national or international format, SIP address, SIP-URI or
     * Tel-URI. If the format of the contact is not supported an exception is thrown.
     *
     * @param contact
     * @param filename Filename to transfer
     * @param fileicon Filename of the file icon associated to the file to be transfered
     * @param listener File transfer event listener
     * @return File transfer
     * @throws JoynServiceException
     * @throws JoynContactFormatException
     */
    public FileTransfer transferGeoLocFile(String contact, String filename, String fileicon, FileTransferListener listener) throws JoynServiceException, JoynContactFormatException {
        /*if (ctx.checkCallingOrSelfPermission(Permissions.RCS_FILETRANSFER_SEND) != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException(" Required permission RCS_FILETRANSFER_SEND");
        }
        */
        Logger.i(TAG, "transferGeoLocFile() entry contact= " + contact + " filename=" + filename + " fileicon = " + fileicon + " listener=" + listener);
        if (api != null) {
            try {
                IFileTransfer ftIntf = api.transferFile(contact, filename, fileicon, listener);
                if (ftIntf != null) {
                    return new FileTransfer(ftIntf);
                } else {
                    return null;
                }
            } catch (Exception e) {
                throw new JoynServiceException(e.getMessage());
            }
        } else {
            throw new JoynServiceNotAvailableException();
        }
    }

   /**
     * Transfers a file to a contact. The parameter filename contains the complete
     * path of the file to be transferred. The parameter contact supports the following
     * formats: MSISDN in national or international format, SIP address, SIP-URI or
     * Tel-URI. If the format of the contact is not supported an exception is thrown.
     *
     * @param contact
     * @param filename Filename to transfer
     * @param fileicon Filename of the file icon associated to the file to be transfered
     * @param listener File transfer event listener
     * @param timeLen Length of the audio/video file
     * @return File transfer
     * @throws JoynServiceException
     * @throws JoynContactFormatException
     */
    public FileTransfer transferPublicChatFile(String contact, String filename, String fileicon, FileTransferListener listener, int timeLen) throws JoynServiceException, JoynContactFormatException {
        /*if (ctx.checkCallingOrSelfPermission(Permissions.RCS_FILETRANSFER_SEND) != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException(" Required permission RCS_FILETRANSFER_SEND");
        }
        */
        Logger.i(TAG, "transferPublicChatFile() entry contact= " + contact + " filename=" + filename + " fileicon = " + fileicon + " listener=" + listener + " ,timelen=" + timeLen);
        if (api != null) {
            try {
                IFileTransfer ftIntf = api.transferPublicChatFile(contact, filename, fileicon, listener, timeLen);
                if (ftIntf != null) {
                    return new FileTransfer(ftIntf);
                } else {
                    return null;
                }
            } catch (Exception e) {
                throw new JoynServiceException(e.getMessage());
            }
        } else {
            throw new JoynServiceNotAvailableException();
        }
    }

    /**
     * transfers a file to a group of contacts outside of a current group chat. The
     * parameter file contains the complete filename including the path to be transferred.
     * See also the method GroupChat.sendFile() of the Chat API to send a file from an
     * existing group chat conversation
     *
     * @param chat id of the group chat
     * @param set of contacts
     * @param filename Filename to transfer
     * @param length of the audio/video file
     * @param listener File transfer event listener
     * @return File transfer
     * @throws JoynServiceException
     */
    public FileTransfer transferFileToGroup(String chatId, Set<String> contacts, String filename, int timeLen, FileTransferListener listener) throws JoynServiceException
    {
        return transferFileToGroup(chatId, contacts, filename, null, timeLen, listener);
    }

    /**
     * transfers a file to a group of contacts with an optional file icon.
     *
     * @param chat id of the group chat
     * @param set of contacts
     * @param filename Filename to transfer
     * @param fileicon Filename of the file icon associated to the file to be transfered
     * @param length of the audio/video file
     * @param listener File transfer event listener
     * @return File transfer
     * @throws JoynServiceException
     */
    public FileTransfer transferFileToGroup(String chatId, Set<String> contacts, String filename, String fileicon, int timeLen, FileTransferListener listener) throws JoynServiceException
    {
        /*if (ctx.checkCallingOrSelfPermission(Permissions.RCS_FILETRANSFER_SEND) != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException(" Required permission RCS_FILETRANSFER_SEND");
        }*/
        Logger.i(TAG, "transferFileToGroup() entry contact= " + contacts + " chatId=" + chatId + " filename=" + filename + " fileicon = " + fileicon + " timelen:" + timeLen + " listener=" + listener);
        if (api != null) {
            try {
                List<String> contactsList = new ArrayList<String>(contacts);
                IFileTransfer ftIntf = api.transferFileToGroup(chatId, contactsList, filename, fileicon, timeLen, listener);
                if (ftIntf != null) {
                    return new FileTransfer(ftIntf);
                } else {
                    return null;
                }
            } catch (Exception e) {
                throw new JoynServiceException(e.getMessage());
            }
        } else {
            throw new JoynServiceNotAvailableException();
        }
    }

    /**
     * transfers a file to a group of contacts outside of a group chat with an optional file icon.
     * @param set of contacts
     * @param filename Filename to transfer
     * @param fileicon Filename of the file icon associated to the file to be transfered
     * @param listener File transfer event listener
     * @return File transfer
     * @throws JoynServiceException
     */
    public FileTransfer transferFileToGroup(Set<String> contacts, String filename, String fileicon, FileTransferListener listener) throws JoynServiceException
    {
        /*if (ctx.checkCallingOrSelfPermission(Permissions.RCS_FILETRANSFER_SEND) != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException(" Required permission RCS_FILETRANSFER_SEND");
        }*/

        if (api != null) {
            try {
            return null;
            } catch (Exception e) {
                throw new JoynServiceException(e.getMessage());
            }
        } else {
            throw new JoynServiceNotAvailableException();
        }
    }

    /**
     * transfers a file to a group of contacts outside of a group chat.
     * @param set of contacts
     * @param filename Filename to transfer
     * @param fileicon Filename of the file icon associated to the file to be transfered
     * @param listener File transfer event listener
     * @return File transfer
     * @throws JoynServiceException
     */
    public FileTransfer transferFileToGroup(Set<String> contacts, String filename, FileTransferListener listener) throws JoynServiceException
    {
        /*if (ctx.checkCallingOrSelfPermission(Permissions.RCS_FILETRANSFER_SEND) != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException(" Required permission RCS_FILETRANSFER_SEND");
        }*/

        if (api != null) {
            try {
            return null;
            } catch (Exception e) {
                throw new JoynServiceException(e.getMessage());
            }
        } else {
            throw new JoynServiceNotAvailableException();
        }
    }

    /**
     * Transfers a media file to a contact. The parameter filename contains the complete
     * path of the file to be transferred. The parameter contact supports the following
     * formats: MSISDN in national or international format, SIP address, SIP-URI or
     * Tel-URI. If the format of the contact is not supported an exception is thrown.
     *
     * @param contact
     * @param filename Filename to transfer
     * @param fileicon Filename of the file icon associated to the file to be transfered
     * @param timeLen Length of audio/video file
     * @param listener File transfer event listener
     * @return File transfer
     * @throws JoynServiceException
     * @throws JoynContactFormatException
     */
    public FileTransfer transferMedia(String contact, String filename, String fileicon, int timeLen, FileTransferListener listener) throws JoynServiceException
    {
            /*if (ctx.checkCallingOrSelfPermission(Permissions.RCS_FILETRANSFER_SEND) != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException(" Required permission RCS_FILETRANSFER_SEND");
        }
        */
        Logger.i(TAG, "transferMedia() entry contact= " + contact + " filename=" + filename + " fileicon = " + fileicon + " timeLen = " + timeLen + " listener=" + listener);
        if (api != null) {
            try {
                IFileTransfer ftIntf = api.transferMedia(contact, filename, fileicon, listener, timeLen);
                if (ftIntf != null) {
                    return new FileTransfer(ftIntf);
                } else {
                    return null;
                }
            } catch (Exception e) {
                throw new JoynServiceException(e.getMessage());
            }
        } else {
            throw new JoynServiceNotAvailableException();
        }
    }

    /**
     * transfers a file to a group of contacts outside of a group chat with an optional file icon.
     *
     * @param set of contacts
     * @param filename Filename to transfer
     * @param fileicon Filename of the file icon associated to the file to be transfered
     * @param listener File transfer event listener
     * @return File transfer
     * @throws JoynServiceException
     */
    public FileTransfer transferFileToMultirecepient(Set<String> contacts, String filename, boolean fileIcon, FileTransferListener listener, int timeLen) throws JoynServiceException
    {
        /*if (ctx.checkCallingOrSelfPermission(Permissions.RCS_FILETRANSFER_SEND) != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException(" Required permission RCS_FILETRANSFER_SEND");
        }*/
        Logger.i(TAG, "transferFileToMultirecepient() entry contact= " + contacts + " filename= " + filename + " listener= " + listener + " Timelen" + timeLen);
        if (api != null) {
            try {
                List<String> contactsList = new ArrayList<String>(contacts);
                IFileTransfer ftIntf = api.transferFileToMultirecepient(contactsList, filename, fileIcon, listener, timeLen);
                if (ftIntf != null) {
                    return new FileTransfer(ftIntf);
                } else {
                    return null;
                }
            } catch (Exception e) {
                throw new JoynServiceException(e.getMessage());
            }
        } else {
            throw new JoynServiceNotAvailableException();
        }
    }

    /**
     * Returns the list of file transfers in progress
     *
     * @return List of file transfers
     * @throws JoynServiceException
     */
    public Set<FileTransfer> getFileTransfers() throws JoynServiceException {
        Logger.i(TAG, "getFileTransfers() entry " + api);
        if (api != null) {
            try {
                Set<FileTransfer> result = new HashSet<FileTransfer>();
                List<IBinder> ftList = api.getFileTransfers();
                for (IBinder binder : ftList) {
                    FileTransfer ft = new FileTransfer(IFileTransfer.Stub.asInterface(binder));
                    result.add(ft);
                }
                return result;
            } catch (Exception e) {
                throw new JoynServiceException(e.getMessage());
            }
        } else {
            throw new JoynServiceNotAvailableException();
        }
    }

    /**
     * Returns a current file transfer from its unique ID
     *
     * @return File transfer or null if not found
     * @throws JoynServiceException
     */
    public FileTransfer getFileTransfer(String transferId) throws JoynServiceException {
        Logger.i(TAG, "getFileTransfer() entry " + transferId + " api=" + api);
        if (api != null) {
            try {
                IFileTransfer ftIntf = api.getFileTransfer(transferId);
                if (ftIntf != null) {
                    return new FileTransfer(ftIntf);
                } else {
                    return null;
                }
            } catch (Exception e) {
                throw new JoynServiceException(e.getMessage());
            }
        } else {
            throw new JoynServiceNotAvailableException();
        }
    }

    /**
     * Returns a current file transfer from its invitation Intent
     *
     * @param intent Invitation intent
     * @return File transfer or null if not found
     * @throws JoynServiceException
     */
    public FileTransfer getFileTransferFor(Intent intent) throws JoynServiceException {
        Logger.i(TAG, "getFileTransferFor() entry " + intent + " api=" + api);
        if (api != null) {
            try {
                String transferId = intent.getStringExtra(FileTransferIntent.EXTRA_TRANSFER_ID);
                if (transferId != null) {
                    return getFileTransfer(transferId);
                } else {
                    return null;
                }
            } catch (Exception e) {
                throw new JoynServiceException(e.getMessage());
            }
        } else {
            throw new JoynServiceNotAvailableException();
        }
    }

    /**
     * Returns true if the service is registered to the platform, else returns
     * false
     *
     * @return Returns true if registered else returns false
     * @throws JoynServiceException
     */
    public void initiateFileSpamReport(String contact, String FtId) throws JoynServiceException {
        Logger.i(TAG, "initiateSpamReport entry " + contact + ":" + FtId);
        if (api != null) {
            try {
                api.initiateFileSpamReport(contact, FtId);
            } catch (Exception e) {
                throw new JoynServiceException(e.getMessage());
            }
        } else {
            throw new JoynServiceNotAvailableException();
        }
    }

    /**
     * Adds a spam listener.
     *
     * @param listener Spam Report listener
     * @throws JoynServiceException
     */
    public void addFileSpamReportListener(FileSpamReportListener listener) throws JoynServiceException {
        Logger.i(TAG, "addSpamReportListener entry" + listener);
        if (api != null) {
            try {
                api.addFileSpamReportListener(listener);
            } catch (Exception e) {
                throw new JoynServiceException(e.getMessage());
            }
        } else {
            throw new JoynServiceNotAvailableException();
        }
    }

    /**
     * Removes a spam listener
     *
     * @param listener Spam Report listener
     * @throws JoynServiceException
     */
    public void removeFileSpamReportListener(FileSpamReportListener listener) throws JoynServiceException {
        Logger.i(TAG, "removeSpamReportListener entry" + listener);
        if (api != null) {
            try {
                api.removeFileSpamReportListener(listener);
            } catch (Exception e) {
                throw new JoynServiceException(e.getMessage());
            }
        } else {
            throw new JoynServiceNotAvailableException();
        }
    }

    /**
     * Registers a file transfer invitation listener
     *
     * @param listener New file transfer listener
     * @throws JoynServiceException
     */
    public void addNewFileTransferListener(NewFileTransferListener listener) throws JoynServiceException {
        /*if (ctx.checkCallingOrSelfPermission(Permissions.RCS_FILETRANSFER_RECEIVE) != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException(" Required permission RCS_FILETRANSFER_RECEIVE");
        }*/
        Logger.i(TAG, "addNewFileTransferListener() entry " + listener + " api=" + api);
        if (api != null) {
            try {
                api.addNewFileTransferListener(listener);
            } catch (Exception e) {
                throw new JoynServiceException(e.getMessage());
            }
        } else {
            throw new JoynServiceNotAvailableException();
        }
    }

    /**
     * Unregisters a file transfer invitation listener
     *
     * @param listener New file transfer listener
     * @throws JoynServiceException
     */
    public void removeNewFileTransferListener(NewFileTransferListener listener) throws JoynServiceException {
        /*if (ctx.checkCallingOrSelfPermission(Permissions.RCS_FILETRANSFER_RECEIVE) != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException(" Required permission RCS_FILETRANSFER_RECEIVE");
        }*/
        Logger.i(TAG, "removeNewFileTransferListener() entry " + listener + " api=" + api);
        if (api != null) {
            try {
                api.removeNewFileTransferListener(listener);
            } catch (Exception e) {
                throw new JoynServiceException(e.getMessage());
            }
        } else {
            throw new JoynServiceNotAvailableException();
        }
    }


    public FileTransfer transferBurnFile(String contact, String filename, String fileicon, FileTransferListener listener) throws JoynServiceException, JoynContactFormatException {
        /*if (ctx.checkCallingOrSelfPermission(Permissions.RCS_FILETRANSFER_SEND) != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException(" Required permission RCS_FILETRANSFER_SEND");
        }
        */
        Logger.i(TAG, "transferBurnFile() entry contact= " + contact + " filename=" + filename + " fileicon = " + fileicon + " listener=" + listener);
        if (api != null) {
            try {
                IFileTransfer ftIntf = api.transferBurnFile(contact, filename, fileicon, listener);
                if (ftIntf != null) {
                    return new FileTransfer(ftIntf);
                } else {
                    return null;
                }
            } catch (Exception e) {
                throw new JoynServiceException(e.getMessage());
            }
        } else {
            throw new JoynServiceNotAvailableException();
        }
    }

}
