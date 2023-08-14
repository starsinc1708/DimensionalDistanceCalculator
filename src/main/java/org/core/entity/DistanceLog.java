package org.core.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "distance_log")
public class DistanceLog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "run_id")
    private Long runId;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "point_a_description")
    private String pointADescription;

    @Column(name = "point_b_description")
    private String pointBDescription;

    @Column(name = "calculation_method")
    private String calculationMethod;

    @Column(name = "distance")
    private Double distance;

    public DistanceLog(Long runId, Date startTime, Date endTime, String pointADescription, String pointBDescription, String calculationMethod, Double distance) {
        this.runId = runId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.pointADescription = pointADescription;
        this.pointBDescription = pointBDescription;
        this.calculationMethod = calculationMethod;
        this.distance = distance;
    }
}
