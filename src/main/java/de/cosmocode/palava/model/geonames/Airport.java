/**
 * palava - a java-php-bridge
 * Copyright (C) 2007  CosmoCode GmbH
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package de.cosmocode.palava.model.geonames;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import de.cosmocode.json.JSONMapable;
import de.cosmocode.json.JSONRenderer;
import de.cosmocode.palava.model.base.ReadOnly;
import de.cosmocode.palava.model.business.AbstractLocation;
import de.cosmocode.palava.model.business.Geographical;
import de.cosmocode.palava.model.business.LocationBase;

/**
 * Airport data for Geonames integration under CC license:
 * <a href="http://www.halfgaar.net/localized-world-airport-codes">Download</a>.
 *
 * @author Willi Schoenborn
 */
@Entity
@Table(name = "geonames_airport")
@ReadOnly
public final class Airport implements Geographical, JSONMapable {
    
    private String code;
    
    private String name;
    
    @Column(name = "city_name")
    private String cityName;
    
    @Column(name = "country_name")
    private String countryName;
    
    @Column(name = "country_code")
    private String countryCode;
    
    @Column(name = "world_area_code")
    private String worldAreaCode;
    
    @JoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private Toponym city;
    
    @JoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private Toponym country;
    
    private Double latitude;
    
    private Double longitude;
    
    @Transient
    private transient LocationBase location;

    /**
     * Pre-persist callback which prevents inserts.
     */
    @PrePersist
    protected void prePersist() {
        throw new UnsupportedOperationException("Airport is read-only");
    }

    /**
     * Pre-update callback which prevents updates.
     */
    @PreUpdate
    protected void preUpdate() {
        throw new UnsupportedOperationException("Airport is read-only");
    }

    /**
     * Pre-delete callback which prevents deletes.
     */
    @PreRemove
    protected void preRemove() {
        throw new UnsupportedOperationException("Airport is read-only");
    }
    
    @Override
    public LocationBase getLocation() {
        if (location == null) {
            location = new InternalLocation();
        }
        return location;
    }
    
    /**
     * Internal implementation of the {@link LocationBase} interface which
     * owns a reference to the enclosing class and is able to directly manipulate the
     * corresponding values.
     *
     * @author Willi Schoenborn
     */
    private final class InternalLocation extends AbstractLocation {
        
        @Override
        public Double getLatitude() {
            return latitude;
        }
        
        @Override
        public void setLatitude(Double latitude) {
            throw new UnsupportedOperationException("Airport is read-only");
        }
        
        @Override
        public Double getLongitude() {
            return longitude;
        }
        
        @Override
        public void setLongitude(Double longitude) {
            throw new UnsupportedOperationException("Airport is read-only");
        }
        
    }
    
    public String getCode() {
        return code;
    }
    
    public String getName() {
        return name;
    }
    
    public String getCityName() {
        return cityName;
    }
    
    public String getCountryName() {
        return countryName;
    }
    
    public String getCountryCode() {
        return countryCode;
    }
    
    public String getWorldAreaCode() {
        return worldAreaCode;
    }
    
    /**
     * Provide the city of this airport.
     * 
     * @return the city of this airport or null if there is no city
     */
    public Toponym getCity() {
        return city;
    }
    
    /**
     * Provide the country of this airport.
     * 
     * @return the country of this airport of null if there is no country
     */
    public Toponym getCountry() {
        return country;
    }
    
    @Override
    public JSONRenderer renderAsMap(JSONRenderer renderer) {
        return renderer.
            key("code").value(getCode()).
            key("name").value(getName()).
            key("cityName").value(getCityName()).
            key("countryName").value(getCountryName()).
            key("countryCode").value(getCountryCode()).
            key("worldAreaCode").value(getWorldAreaCode());
    }

}
