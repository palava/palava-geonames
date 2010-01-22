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

import java.util.Collections;
import java.util.SortedSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.geonames.FeatureClass;

import com.google.common.collect.Sets;

import de.cosmocode.json.JSONRenderer;
import de.cosmocode.palava.model.base.ReadOnly;
import de.cosmocode.palava.model.business.AbstractLocation;
import de.cosmocode.palava.model.business.Geographical;
import de.cosmocode.palava.model.business.LocationBase;
import de.cosmocode.palava.model.geo.AbstractToponym;
import de.cosmocode.palava.model.geo.ToponymBase;

/**
 * Concrete entity implementation of the {@link ToponymBase} interface.
 * 
 * <p>
 *   This class is part of the Palava Geonames package.
 * </p>
 *
 * @author Willi Schoenborn
 */
@Entity
@Table(name = "geonames_toponyms")
@ReadOnly
public final class Toponym extends AbstractToponym implements Geographical {

    @Column(name = "ascii_name")
    private String asciiName;

    @Column(name = "feature_class")
    private FeatureClass featureClass;
    
    @Column(name = "feature_code")
    private String featureCode;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "toponym")
    private SortedSet<AlternateName> alternateNames = Sets.newTreeSet();
    
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
     * Pre-persist callback which prevents updates.
     */
    @PreUpdate
    protected void preUpdate() {
        throw new UnsupportedOperationException("Airport is read-only");
    }

    /**
     * Pre-persist callback which prevents deletes.
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
            throw new UnsupportedOperationException("Toponym is read-only");
        }
        
        @Override
        public Double getLongitude() {
            return longitude;
        }
        
        @Override
        public void setLongitude(Double longitude) {
            throw new UnsupportedOperationException("Toponym is read-only");
        }
        
    }

    public String getAsciiName() {
        return asciiName;
    }
    
    public FeatureClass getFeatureClass() {
        return featureClass;
    }
    
    public String getFeatureCode() {
        return featureCode;
    }
    
    @Override
    public SortedSet<AlternateName> getAliases() {
        return Collections.unmodifiableSortedSet(alternateNames);
    }
    
    @Override
    public JSONRenderer renderAsMap(JSONRenderer renderer) {
        return 
            super.renderAsMap(renderer).
            key("asciiName").value(getAsciiName()).
            key("featureClass").value(getFeatureClass()).
            key("featureCode").value(getFeatureCode()).
            key("location").object(getLocation());
    }
    
}
