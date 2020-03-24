package kpi.prject.testing.testing.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import kpi.prject.testing.testing.entity.enums.ReportStatus;
import kpi.prject.testing.testing.entity.enums.Status;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    @ManyToOne
    @JoinColumn(name = "usr_id")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "inspector_id")
    @JsonBackReference
    private User inspector;


    @CreationTimestamp
    @Column(name = "created", nullable = false)
    private LocalDate created;

    @UpdateTimestamp
    @Column(name = "updated", nullable = false)
    private LocalDate updated;
}
