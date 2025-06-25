package org.zishu.service.admin;

import org.zishu.pojo.Admin;

public interface LoginService {
    boolean authenticate(String username, String password);
}