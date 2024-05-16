package telran.java52.accounting.service;

import telran.java52.accounting.dto.RolesDto;
import telran.java52.accounting.dto.UserDto;
import telran.java52.accounting.dto.UserEditDto;
import telran.java52.accounting.dto.UserRegisterDto;

public interface UserAccountService {
	
//	UserDto registerUser(UserRegisterDto userRegisterDto);
//
//	UserDto loginUser(String user, String password);
//
//	UserDto deleteUser(String user);
//
//	UserDto updateUser(String user,UserEditDto userEditDto);
//
//	RolesDto updateRole(String user, String role);
//
//	RolesDto deleteRole(String user, String role);
//	
//	RolesDto changeRolesList(String user, String role, boolean isActive);
//
//	void changePassword(String user, String password);
//
//	UserDto getUser(String user);
	
	UserDto register(UserRegisterDto userRegisterDto);

	UserDto getUser(String login);

	UserDto removeUser(String login);

	UserDto updateUser(String login, UserEditDto userEditDto);

	RolesDto changeRolesList(String login, String role, boolean isAddRole);

	void changePassword(String login, String newPassword);

}
