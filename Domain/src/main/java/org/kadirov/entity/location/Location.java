package org.kadirov.entity.location;

import jakarta.persistence.*;
import lombok.*;
import org.kadirov.entity.user.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    public Location(String name, User user, Double latitude, Double longitude) {
        this.name = name;
        this.user = user;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
