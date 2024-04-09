package com.fxa.image.common.message;

import java.io.Serializable;

public class TestMessage  implements Serializable{

        public TestMessage(){}

        public TestMessage(String str){
            msg = str;
        }

        private String msg;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

}
