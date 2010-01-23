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

import com.google.common.base.Function;
import com.google.common.collect.Ordering;

import de.cosmocode.json.JSONRenderer;
import de.cosmocode.palava.model.base.ReadOnly;
import de.cosmocode.palava.model.geo.AliasBase;

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
    public JSONRenderer renderAsMap(JSONRenderer renderer) {
        return renderer.
            key("name").value(getName()).
            key("languageCode").value(getLanguageCode()).
            key("isPreferredName").value(isPreferredName()).
            key("isShortName").value(isShortName());
    }

}
