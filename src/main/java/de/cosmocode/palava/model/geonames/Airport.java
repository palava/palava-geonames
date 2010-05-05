/**
 * Copyright 2010 CosmoCode GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

import de.cosmocode.palava.model.base.ReadOnly;
import de.cosmocode.palava.model.business.AbstractLocation;
import de.cosmocode.palava.model.business.Geographical;
import de.cosmocode.palava.model.business.Location;
import de.cosmocode.rendering.Renderer;
import de.cosmocode.rendering.RenderingException;
import de.cosmocode.rendering.RenderingLevel;

/**
 * Airport data for Geonames integration under CC license:
 * <a href="http://www.halfgaar.net/localized-world-airport-codes">Download</a>.
 *
 * @author Willi Schoenborn
 */
@Entity
@Table(name = "geonames_airport")
@ReadOnly
public final class Airport implements Geographical {
    
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
    private transient Location location;

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
    public Location getLocation() {
        if (location == null) {
            location = new InternalLocation();
        }
        return location;
    }
    
    /**
     * Internal implementation of the {@link Location} interface which
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
    public void render(Renderer renderer, RenderingLevel level) throws RenderingException {
        renderer.
            key("code").value(getCode()).
            key("name").value(getName()).
            key("cityName").value(getCityName()).
            key("countryName").value(getCountryName()).
            key("countryCode").value(getCountryCode()).
            key("worldAreaCode").value(getWorldAreaCode());
    }

}
