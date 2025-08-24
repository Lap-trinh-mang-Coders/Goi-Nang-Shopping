package com.example.goinangshopping.service;

import com.example.goinangshopping.dto.request.ChangePasswordRequest;
import com.example.goinangshopping.dto.request.UserCreateDto;
import com.example.goinangshopping.dto.response.UserResponseDto;
import com.example.goinangshopping.exceptions.AppException;
import com.example.goinangshopping.exceptions.ErrorCode;
import com.example.goinangshopping.model.User;
import com.example.goinangshopping.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public  class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Convert Entity to DTO
    public UserResponseDto convertToDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setUsername(user.getUsername());
        userResponseDto.setFullname(user.getFullname());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setPhone(user.getPhone());
        userResponseDto.setRoles(user.getRoles());
        return userResponseDto;
    }

    // Tạo người dùng mới
    public UserResponseDto createUser(@Valid UserCreateDto userCreateDto) {
        // Kiểm tra username đã tồn tại
        if (userRepository.existsByUsername(userCreateDto.getUsername())) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }

        // Kiểm tra email đã tồn tại
        if (userRepository.existsByEmail(userCreateDto.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        User user = new User();
        user.setUsername(userCreateDto.getUsername());
        user.setFullname(userCreateDto.getFullname());
        user.setEmail(userCreateDto.getEmail());
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        user.setPhone(userCreateDto.getPhone());

        // Set default role if not provided
        if (userCreateDto.getRoles() == null || userCreateDto.getRoles().isEmpty()) {
            user.setRoles(Set.of(User.Roles.USER));
        } else {
            user.setRoles(userCreateDto.getRoles());
        }

        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    // Lấy thông tin người dùng theo ID
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return convertToDto(user);
    }

    // Lấy thông tin người dùng theo username
    public UserResponseDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return convertToDto(user);
    }

    // Lấy thông tin người dùng theo email
    public UserResponseDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return convertToDto(user);
    }

    // Lấy danh sách tất cả người dùng
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Cập nhật thông tin người dùng theo ID
    public UserResponseDto updateUser(Long id, UserCreateDto userCreateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Kiểm tra username conflict (nếu username thay đổi)
        if (!user.getUsername().equals(userCreateDto.getUsername()) &&
                userRepository.existsByUsername(userCreateDto.getUsername())) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }

        // Kiểm tra email conflict (nếu email thay đổi)
        if (!user.getEmail().equals(userCreateDto.getEmail()) &&
                userRepository.existsByEmail(userCreateDto.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        user.setUsername(userCreateDto.getUsername());
        user.setFullname(userCreateDto.getFullname());
        user.setPhone(userCreateDto.getPhone());
        user.setEmail(userCreateDto.getEmail());

        User updatedUser = userRepository.save(user);
        return convertToDto(updatedUser);
    }

    // Xóa người dùng theo ID
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        userRepository.deleteById(id);
    }

    // Thay đổi mật khẩu của người dùng theo ID
    public void changePassword(Long id, ChangePasswordRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // So sánh mật khẩu cũ
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_INVALID);
        }

        // Gán mật khẩu mới (đã mã hóa)
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }


    // Kiểm tra username tồn tại
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    // Kiểm tra email tồn tại
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}