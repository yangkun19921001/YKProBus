package com.devyk.component_eventbus.proevent.entity;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * <pre>
 *     author  : devyk on 2019-08-09 14:02
 *     blog    : https://juejin.im/user/578259398ac2470061f3a3fb/posts
 *     github  : https://github.com/yangkun19921001
 *     mailbox : yang1001yk@gmail.com
 *     desc    : This is BaseMessageEntity 负责在 服务端跟客服端传递消息的实体类
 * </pre>
 */
public class BaseMessageEntity  implements Parcelable {

    /**
     * 消息类型，也就是注册的那个类中
     */
    private int messageType;

    /**
     * 消息实体
     */
    private Bundle message;

    public BaseMessageEntity(int messageType, Bundle message) {
        this.messageType = messageType;
        this.message = message;
    }

    public BaseMessageEntity() {
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public Bundle getMessage() {
        return message;
    }

    public void setMessage(Bundle message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.messageType);
        dest.writeBundle(this.message);
    }

    protected BaseMessageEntity(Parcel in) {
        this.messageType =  in.readInt();
        this.message = in.readBundle();
    }

    public static final Creator<BaseMessageEntity> CREATOR = new Creator<BaseMessageEntity>() {
        @Override
        public BaseMessageEntity createFromParcel(Parcel source) {
            return new BaseMessageEntity(source);
        }

        @Override
        public BaseMessageEntity[] newArray(int size) {
            return new BaseMessageEntity[size];
        }
    };
}
