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
                technicalModel.getName(),
                technicalModel.getEmail(),
                technicalModel.getPhoneNumber(),
                technicalModel.getDateBirth(),
                technicalModel.getProfilePicture(),
                technicalModel.getRole(),
                technicalModel.getUserStatus()
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
        Technical technical = new Technical();
        technical.setName(technicalRegisterRequest.name());
        technical.setEmail(technicalRegisterRequest.email());
        technical.setDateBirth(technicalRegisterRequest.dateBirth());
        technical.setPassword(technicalRegisterRequest.password());
        technical.setPhoneNumber(technicalRegisterRequest.phoneNumber());
        technical.setProfilePicture(technicalRegisterRequest.profilePicture());
        technical.setUserStatus(UserStatusEnum.ACTIVE.getValue());

        return technical;
    }

    public Page<TechnicalResponseDTO> toResponsePageDTO(Page<Technical> technicalsEntityPage) {
        return technicalsEntityPage.map(this::toResponseDTO);
    }
}
