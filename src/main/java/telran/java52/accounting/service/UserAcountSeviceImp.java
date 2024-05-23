package telran.java52.accounting.service;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java52.accounting.dao.UserRepository;
import telran.java52.accounting.dto.RolesDto;
import telran.java52.accounting.dto.UserDto;
import telran.java52.accounting.dto.UserEditDto;
import telran.java52.accounting.dto.UserRegisterDto;
import telran.java52.accounting.exceptions.IncorrectRoleException;
import telran.java52.accounting.exceptions.LoginUsedException;
import telran.java52.accounting.exceptions.UserNotFoundException;
import telran.java52.accounting.model.Role;
import telran.java52.accounting.model.UserAccount;

@Service
@RequiredArgsConstructor
public class UserAcountSeviceImp implements UserAccountService, CommandLineRunner {

	final UserRepository userRepository;
	final ModelMapper modelMapper;

	@Override
	public UserDto register(UserRegisterDto userRegisterDto) {
		if (userRepository.findById(userRegisterDto.getLogin()).isEmpty()) {
			UserAccount user = modelMapper.map(userRegisterDto, UserAccount.class);
			String password = BCrypt.hashpw(userRegisterDto.getPassword(), BCrypt.gensalt());
			user.setPassword(password);
			userRepository.save(user);
			return modelMapper.map(user, UserDto.class);
		}
		throw new LoginUsedException();

	}

	@Override
	public UserDto getUser(String login) {
		UserAccount user = userRepository.findById(login).orElseThrow(UserNotFoundException::new);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto removeUser(String login) {
		UserAccount user = userRepository.findById(login).orElseThrow(UserNotFoundException::new);
		userRepository.delete(user);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto updateUser(String login, UserEditDto userEditDto) {
		UserAccount user = userRepository.findById(login).orElseThrow(UserNotFoundException::new);
		if (userEditDto.getFirstName() != null) {
			user.setFirstName(userEditDto.getFirstName());
		}
		if (userEditDto.getLastName() != null) {
			user.setLastName(userEditDto.getLastName());
		}
		userRepository.save(user);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public RolesDto changeRolesList(String login, String role, boolean isAddRole) {
		UserAccount user = userRepository.findById(login).orElseThrow(UserNotFoundException::new);
		try {
			if (isAddRole) {
				user.addRole(role);
			} else {
				user.removeRole(role);
			}
		} catch (Exception e) {
			throw new IncorrectRoleException();
		}
		userRepository.save(user);
		return modelMapper.map(user, RolesDto.class);
	}

	@Override
	public void changePassword(String login, String newPassword) {
		UserAccount user = userRepository.findById(login).orElseThrow(UserNotFoundException::new);
		if (newPassword != null) {
			String password = BCrypt.hashpw(newPassword, BCrypt.gensalt());
			user.setPassword(password);
			userRepository.save(user);
		}

	}

	@Override
	public void run(String... args) throws Exception {
		if(!userRepository.existsById("admin")) {
			String password = BCrypt.hashpw("admin", BCrypt.gensalt());
			UserAccount userAccount = new UserAccount("admin", password, "", "");
			userAccount.addRole(Role.MODERATOR.name());
			userAccount.addRole(Role.ADMINISTRATOR.name());
			userRepository.save(userAccount);
		}
		
	}

}
