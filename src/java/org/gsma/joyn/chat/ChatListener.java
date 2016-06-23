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
package org.gsma.joyn.chat;


/**
 * Chat event listener
 *
 * @author Jean-Marc AUFFRET
 */
public abstract class ChatListener extends IChatListener.Stub {
    /**
     * Callback called when a new message has been received
     *
     * @param message Chat message
     * @see ChatMessage
     */
    public abstract void onNewMessage(ChatMessage message);

    /**
     * Callback called when a new geoloc has been received
     *
     * @param message Geoloc message
     */
    public abstract void onNewGeoloc(GeolocMessage message);

    /**
     * Callback called when a message has been delivered to the remote
     *
     * @param msgId Message ID
     */
    public abstract void onReportMessageDelivered(String msgId);

    /**
     * Callback called when a message has been displayed by the remote
     *
     * @param msgId Message ID
     */
    public abstract void onReportMessageDisplayed(String msgId);

    /**
     * Callback called when a message has failed to be delivered to the remote
     *
     * @param msgId Message ID
     */
    public abstract void onReportMessageFailed(String msgId);

    /**
     * Callback called when an Is-composing event has been received. If the
     * remote is typing a message the status is set to true, else it is false.
     *
     * @param status Is-composing status
     */
    public abstract void onComposingEvent(boolean status);

    /**
     * Callback called when a message has failed to be delivered to the remote
     *
     * @param msgId Message ID
     */
    public void onReportFailedMessage(String msgId, int errtype, String statusCode) {
        // default implementation for TAPI extension
    }

    /**
     * Callback called when a message has been sent to the remote
     *
     * @param msgId Message ID
     */
    public void onReportSentMessage(String msgId) {
        // default implementation for TAPI extension
    }

    /**
     * Callback called when a message has failed to be delivered to the remote
     *
     * @param msgId Message ID
     */
    public void onReportDeliveredMessage(String msgId) {
        // default implementation for TAPI extension
    }

    /**
     * Callback called when a new burn message has been received
     *
     * @param message Chat message
     * @see ChatMessage
     */
    public void onNewBurnMessageArrived(ChatMessage msg) {
        // default implementation for TAPI extension
    }

    //message callback when a new burn file transfer arrives
    //public abstract void onNewBurnFileTransfer(
    //          String transferId,Boolean isGroup,String chatSessionId,String ChatId);
}
