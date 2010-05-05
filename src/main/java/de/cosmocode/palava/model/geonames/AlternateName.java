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

import com.google.common.base.Function;
import com.google.common.collect.Ordering;

import de.cosmocode.palava.model.base.ReadOnly;
import de.cosmocode.palava.model.geo.AliasBase;
import de.cosmocode.rendering.Renderer;
import de.cosmocode.rendering.RenderingException;
import de.cosmocode.rendering.RenderingLevel;

/**
 * Concrete entity implementation of the {@link AliasBase} interface.
 * 
 * <p>
 *   This class is part of the Palava Geonames package.
 * </p>
 *
 * @author Willi Schoenborn
 */
@Entity
@Table(name = "geonames_alternate_names")
@ReadOnly
public final class AlternateName implements AliasBase, Comparable<AlternateName> {
    
    private static final Ordering<AlternateName> ORDERING = 
        Ordering.natural().onResultOf(new Function<AlternateName, Boolean>() {
            
            @Override
            public Boolean apply(AlternateName from) {
                return Boolean.valueOf(from.isPreferredName());
            }
            
        }).reverse().compound(Ordering.natural().onResultOf(new Function<AlternateName, String>() {
    
            @Override
            public String apply(AlternateName from) {
                return from.getName();
            }
            
        }).nullsLast());

    private String name;
    
    @Column(name = "language_code")
    private String languageCode;
    
    @JoinColumn
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Toponym toponym;

    @Column(name = "preferred_name")
    private boolean preferredName;
    
    @Column(name = "short_name")
    private boolean shortName;

    /**
     * Pre-persist callback which prevents inserts.
     */
    @PrePersist
    protected void prePersist() {
        throw new UnsupportedOperationException("AlternateName is read-only");
    }

    /**
     * Pre-update callback which prevents updates.
     */
    @PreUpdate
    protected void preUpdate() {
        throw new UnsupportedOperationException("AlternateName is read-only");
    }

    /**
     * Pre-remove callback which prevents deletes.
     */
    @PreRemove
    protected void preRemove() {
        throw new UnsupportedOperationException("AlternateName is read-only");
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getLanguageCode() {
        return languageCode;
    }
    
    @Override
    public Toponym getToponym() {
        return toponym;
    }

    public boolean isPreferredName() {
        return preferredName;
    }
    
    public boolean isShortName() {
        return shortName;
    }
    
    @Override
    public int compareTo(AlternateName that) {
        return ORDERING.compare(this, that);
    }
    
    @Override
    public void render(Renderer renderer, RenderingLevel level) throws RenderingException {
        renderer.
            key("name").value(getName()).
            key("languageCode").value(getLanguageCode()).
            key("isPreferredName").value(isPreferredName()).
            key("isShortName").value(isShortName());
    }

}
