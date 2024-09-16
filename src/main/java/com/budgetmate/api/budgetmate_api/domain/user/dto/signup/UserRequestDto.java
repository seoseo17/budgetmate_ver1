package com.budgetmate.api.budgetmate_api.domain.user.dto.signup;

import com.budgetmate.api.budgetmate_api.domain.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserRequestDto {

    @NotBlank(message = "필수 입력값입니다.")
    @Email(message = "이메일 형식이 바르지 않습니다.")
    private String email;

    @NotBlank(message = "필수 입력값입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "필수 입력값입니다.")
    @Size(min = 2, max = 20, message = "계정은 2자 이상, 20자 이하로 입력해야 합니다.")
    private String nickname;

}
