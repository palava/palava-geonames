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

import de.cosmocode.palava.model.base.ReadOnly;
import de.cosmocode.palava.model.business.AbstractLocation;
import de.cosmocode.palava.model.business.Geographical;
import de.cosmocode.palava.model.business.Location;
import de.cosmocode.palava.model.geo.AbstractToponym;
import de.cosmocode.palava.model.geo.ToponymBase;
import de.cosmocode.rendering.Renderer;
import de.cosmocode.rendering.RenderingException;
import de.cosmocode.rendering.RenderingLevel;

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
    public void render(Renderer renderer, RenderingLevel level) throws RenderingException {
        super.render(renderer, level);
        renderer.
            key("asciiName").value(getAsciiName()).
            key("featureClass").value(getFeatureClass()).
            key("featureCode").value(getFeatureCode()).
            key("location").value(getLocation(), level);
    }
    
}
