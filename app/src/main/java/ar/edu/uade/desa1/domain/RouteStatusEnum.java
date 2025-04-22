package ar.edu.uade.desa1.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum RouteStatusEnum {

    AVAILABLE("Available", "Disponible"),
    IN_PROGRESS("In progress", "En progreso"),
    COMPLETED("Completed", "Finalizada");

    private String englishStatus;
    private String spanishStatus;
}