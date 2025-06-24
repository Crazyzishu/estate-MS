package org.zishu.service.admin;

public interface LoginService {
    boolean authenticate(String username, String password);
}