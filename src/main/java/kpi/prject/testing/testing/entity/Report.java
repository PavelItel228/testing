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
import java.util.List;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "report_inspectors",
                joinColumns = {@JoinColumn(name = "usr_id")},
                inverseJoinColumns = {@JoinColumn(name = "report_id")})
    private List<User> inspectors;


    @CreationTimestamp
    @Column(name = "created", nullable = false)
    private LocalDate created;

    @UpdateTimestamp
    @Column(name = "updated", nullable = false)
    private LocalDate updated;

    @Column(name = "decline_reason", columnDefinition = "TEXT")
    private String reason;

    @OneToMany(mappedBy = "report")
    private List<Archive> archive;
}
