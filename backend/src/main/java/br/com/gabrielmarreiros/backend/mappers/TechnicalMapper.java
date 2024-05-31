package br.com.gabrielmarreiros.backend.mappers;

import br.com.gabrielmarreiros.backend.dto.technical.TechnicalRegisterRequestDTO;
import br.com.gabrielmarreiros.backend.dto.technical.TechnicalRegisterResponseDTO;
import br.com.gabrielmarreiros.backend.dto.technical.TechnicalResponseDTO;
import br.com.gabrielmarreiros.backend.enums.UserStatusEnum;
import br.com.gabrielmarreiros.backend.models.Technical;
import br.com.gabrielmarreiros.backend.models.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TechnicalMapper {

    public TechnicalRegisterResponseDTO toRegisterResponseDTO(Technical technicalModel, String token){
        TechnicalResponseDTO technicalResponse = this.toResponseDTO(technicalModel);

        return new TechnicalRegisterResponseDTO(
                        technicalResponse,
                        token
                    );
    }

    public TechnicalResponseDTO toResponseDTO(Technical technicalModel){
        return new TechnicalResponseDTO(
                technicalModel.getId(),
                technicalModel.getUser().getId(),
                technicalModel.getUser().getName(),
                technicalModel.getUser().getEmail(),
                technicalModel.getUser().getPhoneNumber(),
                technicalModel.getDateBirth(),
                technicalModel.getUser().getProfilePicture(),
                technicalModel.getUser().getRole(),
                technicalModel.getUser().getUserStatus()
        );
    }

    public List<TechnicalResponseDTO> toResponseListDTO(List<Technical> technicalList){
        List<TechnicalResponseDTO> technicalResponseList = technicalList
                                                                .stream()
                                                                .map(this::toResponseDTO)
                                                                .toList();

        return technicalResponseList;
    }

    public Technical toEntity(TechnicalRegisterRequestDTO technicalRegisterRequest){
        User user = new User();
        user.setName(technicalRegisterRequest.name());
        user.setEmail(technicalRegisterRequest.email());
        user.setPassword(technicalRegisterRequest.password());
        user.setPhoneNumber(technicalRegisterRequest.phoneNumber());
        user.setProfilePicture(technicalRegisterRequest.profilePicture());
        user.setUserStatus(UserStatusEnum.ACTIVE.getValue());

        Technical technical = new Technical();
        technical.setDateBirth(technicalRegisterRequest.dateBirth());
        technical.setUser(user);

        return technical;
    }

    public Page<TechnicalResponseDTO> toResponsePageDTO(Page<Technical> technicalsEntityPage) {
        return technicalsEntityPage.map(this::toResponseDTO);
    }
}
