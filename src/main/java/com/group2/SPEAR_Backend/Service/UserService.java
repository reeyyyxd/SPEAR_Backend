package com.group2.SPEAR_Backend.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.group2.SPEAR_Backend.Model.User;
import com.group2.SPEAR_Backend.Repository.UserRepository;
import com.group2.SPEAR_Backend.Security.JWTUtil;
import com.group2.SPEAR_Backend.DTO.UserDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;



import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {


    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepo.findByEmail(email).orElseThrow();
    }


    public UserDTO register(UserDTO registrationRequest) {
        UserDTO resp = new UserDTO();

        try {
            User ourUser = new User();
            ourUser.setEmail(registrationRequest.getEmail());
            ourUser.setRole(registrationRequest.getRole());
            ourUser.setFirstname(registrationRequest.getFirstname());
            ourUser.setLastname(registrationRequest.getLastname());
            ourUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));

            if ("TEACHER".equalsIgnoreCase(ourUser.getRole())) {
                ourUser.setInterests(registrationRequest.getInterests());
            }

            User ourUsersResult = userRepo.save(ourUser);
            if (ourUsersResult.getUid() > 0) {
                resp.setUser((ourUsersResult));
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }



    public UserDTO login(UserDTO loginRequest){
        UserDTO response = new UserDTO();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            User user = userRepo.findByEmail(loginRequest.getEmail()).orElseThrow();
            String jwt = jwtUtil.generateToken(user);
            String refreshToken = jwtUtil.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRole(user.getRole());
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hrs");
            response.setMessage("Successfully Logged In");

            // Set the UID in the response
            response.setUid(user.getUid());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }


    public UserDTO refreshToken(UserDTO refreshTokenRequest){
        UserDTO response = new UserDTO();
        try{
            String ourEmail = jwtUtil.extractUsername(refreshTokenRequest.getToken());
            User users = userRepo.findByEmail(ourEmail).orElseThrow();
            if (jwtUtil.isTokenValid(refreshTokenRequest.getToken(), users)) {
                var jwt = jwtUtil.generateToken(users);
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenRequest.getToken());
                response.setExpirationTime("24Hr");
                response.setMessage("Successfully Refreshed Token");
            }
            response.setStatusCode(200);
            return response;

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return response;
        }
    }


    public UserDTO getAllUsers() {
        UserDTO userDTO = new UserDTO();
        try {
            List<User> result = userRepo.findByIsDeletedFalse();
            if (!result.isEmpty()) {
                userDTO.setUserList(result);
                userDTO.setStatusCode(200);
                userDTO.setMessage("Successful");
            } else {
                userDTO.setStatusCode(404);
                userDTO.setMessage("No users found");
            }
            return userDTO;
        } catch (Exception e) {
            userDTO.setStatusCode(500);
            userDTO.setMessage("Error occurred: " + e.getMessage());
            return userDTO;
        }
    }

    public UserDTO getUsersById(Integer id) {
        UserDTO userDTO = new UserDTO();
        try {
            User usersById = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not found"));
            userDTO.setUser(usersById);
            userDTO.setStatusCode(200);
            userDTO.setMessage("Users with id '" + id + "' found successfully");
            userDTO.setDeleted(usersById.isDeleted());
        } catch (Exception e) {
            userDTO.setStatusCode(500);
            userDTO.setMessage("Error occurred: " + e.getMessage());
        }
        return userDTO;
    }



    public UserDTO deleteUser(Integer userId) {
        UserDTO userDTO = new UserDTO();
        try {
            Optional<User> userOptional = userRepo.findById(userId);
            if (userOptional.isPresent()) {
                User existingUser = userOptional.get();
                existingUser.setIsDeleted(true);
                userRepo.save(existingUser);

                userDTO.setStatusCode(200);
                userDTO.setMessage("User deleted successfully");
            } else {
                userDTO.setStatusCode(404);
                userDTO.setMessage("User not found");
            }
        } catch (Exception e) {
            userDTO.setStatusCode(500);
            userDTO.setMessage("Error occurred:" + e.getMessage());
        }
        return userDTO;
    }

    public UserDTO updateAdmin(Integer userId, User updatedUser) {
        UserDTO userDTO = new UserDTO();
        try {
            User existingUser = userRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (updatedUser.getEmail() != null) existingUser.setEmail(updatedUser.getEmail());
            if (updatedUser.getFirstname() != null) existingUser.setFirstname(updatedUser.getFirstname());
            if (updatedUser.getLastname() != null) existingUser.setLastname(updatedUser.getLastname());
            if (updatedUser.getRole() != null) existingUser.setRole(updatedUser.getRole());
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }
            if (updatedUser.getInterests() != null) existingUser.setInterests(updatedUser.getInterests());
            existingUser.setIsDeleted(updatedUser.isDeleted());

            User savedUser = userRepo.save(existingUser);
            userDTO.setUser(savedUser);
            userDTO.setStatusCode(200);
            userDTO.setMessage("User updated successfully by Admin");
        } catch (Exception e) {
            userDTO.setStatusCode(500);
            userDTO.setMessage("Error occurred: " + e.getMessage());
        }
        return userDTO;
    }

    public UserDTO updateTeacher(Integer userId, User updatedUser) {
        UserDTO userDTO = new UserDTO();
        try {
            User existingUser = userRepo.findById(userId)
                    .filter(user -> "TEACHER".equalsIgnoreCase(user.getRole()))
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));

            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setFirstname(updatedUser.getFirstname());
            existingUser.setLastname(updatedUser.getLastname());

            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            if (updatedUser.getInterests() != null && !updatedUser.getInterests().isEmpty()) {
                existingUser.setInterests(updatedUser.getInterests());
            } else {
                existingUser.setInterests("Teachers only");
            }

            User savedUser = userRepo.save(existingUser);
            userDTO.setUser(savedUser);
            userDTO.setStatusCode(200);
            userDTO.setMessage("User updated successfully by Teacher");
        } catch (Exception e) {
            userDTO.setStatusCode(500);
            userDTO.setMessage("Error occurred: " + e.getMessage());
        }
        return userDTO;
    }

    public UserDTO updateStudent(Integer userId, User updatedUser) {
        UserDTO userDTO = new UserDTO();
        try {
            User existingUser = userRepo.findById(userId)
                    .filter(user -> "STUDENT".equalsIgnoreCase(user.getRole()))
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setFirstname(updatedUser.getFirstname());
            existingUser.setLastname(updatedUser.getLastname());

            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            User savedUser = userRepo.save(existingUser);
            userDTO.setUser(savedUser);
            userDTO.setStatusCode(200);
            userDTO.setMessage("User updated successfully by Student");
        } catch (Exception e) {
            userDTO.setStatusCode(500);
            userDTO.setMessage("Error occurred: " + e.getMessage());
        }
        return userDTO;
    }



    public UserDTO getMyInfo(String email){
        UserDTO userDTO = new UserDTO();
        try {
            Optional<User> userOptional = userRepo.findByEmail(email);
            if (userOptional.isPresent()) {
                userDTO.setUser(userOptional.get());
                userDTO.setStatusCode(200);
                userDTO.setMessage("successful");
            } else {
                userDTO.setStatusCode(404);
                userDTO.setMessage("User not found for update");
            }

        }catch (Exception e){
            userDTO.setStatusCode(500);
            userDTO.setMessage("Error occurred while getting user info: " + e.getMessage());
        }
        return userDTO;

    }

    public String getInterestsByTeacherId(int teacherId) {
        return userRepo.findInterestsByTeacherId(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found or no interests set"));
    }


    public List<UserDTO> getAllActiveUsers() {
        return userRepo.fetchAllUsersNotDeleted();
    }

    public List<UserDTO> getAllActiveStudents() {
        return userRepo.fetchAllActiveStudents();
    }

    public List<UserDTO> getAllActiveTeachers() {
        return userRepo.fetchAllActiveTeachers();
    }

    public List<UserDTO> getAllSoftDeletedTeachers() {
        return userRepo.fetchAllSoftDeletedTeachers();
    }

    public List<UserDTO> getAllSoftDeletedStudents() {
        return userRepo.fetchAllSoftDeletedStudents();
    }




}