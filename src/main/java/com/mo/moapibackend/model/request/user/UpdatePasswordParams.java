package com.mo.moapibackend.model.request.user;

import lombok.Data;

import java.io.Serializable;
@Data
public class UpdatePasswordParams implements Serializable {

    private static final long serialVersionUID = 5918217680805083859L;

    private String oldPassword;

    private String newPassword;

    private String checkPassword;
}
