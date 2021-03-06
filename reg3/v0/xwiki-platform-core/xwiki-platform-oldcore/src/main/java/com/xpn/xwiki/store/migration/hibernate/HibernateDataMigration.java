/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.xpn.xwiki.store.migration.hibernate;

import org.xwiki.component.annotation.Role;

import com.xpn.xwiki.store.migration.DataMigration;
import com.xpn.xwiki.store.migration.DataMigrationException;

/**
 * Hibernate data migrations role.
 *
 * @version $Id: 22bc750a0e9e6948d0411a12e869b06446708e33 $
 * @since 3.4M1
 */
@Role
public interface HibernateDataMigration extends DataMigration
{
    /**
     * @return some liquibase changelogs for refactoring the database before the hibernate schema update is processed
     * @throws com.xpn.xwiki.store.migration.DataMigrationException on error
     * @since 4.3
     */
    String getPreHibernateLiquibaseChangeLog() throws DataMigrationException;

    /**
     * @return some liquibase changelogs for refactoring the database after the hibernate schema update has been
     *         processed
     * @throws com.xpn.xwiki.store.migration.DataMigrationException on error
     * @since 4.0M1
     */
    String getLiquibaseChangeLog() throws DataMigrationException;
}
