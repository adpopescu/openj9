/*[INCLUDE-IF SharedClasses]*/
/*
 * Copyright IBM Corp. and others 1998
 *
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which accompanies this
 * distribution and is available at https://www.eclipse.org/legal/epl-2.0/
 * or the Apache License, Version 2.0 which accompanies this distribution and
 * is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * This Source Code may also be made available under the following
 * Secondary Licenses when the conditions for such availability set
 * forth in the Eclipse Public License, v. 2.0 are satisfied: GNU
 * General Public License, version 2 with the GNU Classpath
 * Exception [1] and GNU General Public License, version 2 with the
 * OpenJDK Assembly Exception [2].
 *
 * [1] https://www.gnu.org/software/classpath/license.html
 * [2] https://openjdk.org/legal/assembly-exception.html
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0 OR GPL-2.0-only WITH Classpath-exception-2.0 OR GPL-2.0-only WITH OpenJDK-assembly-exception-1.0
 */
package com.ibm.oti.shared;

import java.net.URL;
/*[IF JAVA_SPEC_VERSION >= 9]*/
import java.util.Set;
/*[ENDIF] JAVA_SPEC_VERSION >= 9 */

/**
 * <p>SharedClassHelperFactory provides an interface that is
 * used to create various types of SharedClassHelper for ClassLoaders.</p>
 *
/*[IF JAVA_SPEC_VERSION >= 9]
 * <p>ClassLoaders and each type of SharedClassHelpers have a one-to-one relationship.</p>
/*[ELSE] JAVA_SPEC_VERSION >= 9
 * <p>ClassLoaders and SharedClassHelpers have a one-to-one relationship.
 * Any attempts to get a helper for a ClassLoader that already has a different type of helper
 * will result in a HelperAlreadyDefinedException.</p>
/*[ENDIF] JAVA_SPEC_VERSION >= 9
 *
 * There are 3 different types of SharedClassHelper:<br>
 * <ol>
 * <li>SharedClassTokenHelper<br>
 * Stores and finds classes using a String token generated by the ClassLoader.
 * For use by ClassLoaders that require complete control over cache contents.</li>
 *
 * <li>SharedClassURLHelper<br>
 * Stores and finds classes using a URL location. Any URL can be used to store or find classes.<br>
 * For use by ClassLoaders that do not have the concept of a classpath, which load classes from multiple locations.<br>
 * Classes can only be stored using jar/zip or file URLs. Classes are automatically kept up-to-date by the cache.<br>
 * Classes stored using SharedClassURLClasspathHelper can be found by using this helper, and vice versa.</li>
 *
 * <li>SharedClassURLClasspathHelper<br>
 * Stores and finds classes using a URL classpath. URLs can be appended to the classpath at any time.
 * The classpath can also be modified under certain circumstances (see SharedClassURLClasspathHelper javadoc).<br>
 * For use by ClassLoaders that load classes by using a URL classpath.<br>
 * Classes can only be stored using jar/zip or file URLs. Classes are automatically kept up-to-date by the cache.<br>
 * Classes stored using SharedClassURLHelper can be found using this helper, and vice versa.</li>
 * </ol>
 *
 * @see SharedClassTokenHelper
 * @see SharedClassURLHelper
 * @see SharedClassURLClasspathHelper
 */
public interface SharedClassHelperFactory {

	/**
	 * <p>Returns a SharedClassTokenHelper for a given ClassLoader.</p>
	 * <p>Creates a new SharedClassTokenHelper if one cannot be found, otherwise returns an existing SharedClassTokenHelper.</p>
	 * <p>Throws a HelperAlreadyDefinedException if the ClassLoader already has a different type of helper.</p>
	/*[IF JAVA_SPEC_VERSION < 24]
	 * <p>Returns null if a SecurityManager is installed and there is no
	 * SharedClassPermission for the ClassLoader specified.</p>
	/*[ENDIF] JAVA_SPEC_VERSION < 24
	 *
	 * @see SharedClassTokenHelper
	 *
	 * @param 		loader ClassLoader.
	 * 					ClassLoader to which this SharedClassTokenHelper will belong
	 *
	 * @return		SharedClassTokenHelper.
	 * 					A new or existing SharedClassTokenHelper
	 *
	/*[IF JAVA_SPEC_VERSION == 8]
	 * @throws 		HelperAlreadyDefinedException
	 * 					when another helper has been already defined for this class loader.
	/*[ENDIF] JAVA_SPEC_VERSION == 8
	 */
	public SharedClassTokenHelper getTokenHelper(ClassLoader loader)
	/*[IF JAVA_SPEC_VERSION == 8]*/
			throws HelperAlreadyDefinedException
	/*[ENDIF] JAVA_SPEC_VERSION == 8 */
	;

	/**
	 * <p>Returns a SharedClassTokenHelper for a given ClassLoader.</p>
	 * <p>Creates a new SharedClassTokenHelper if one cannot be found, otherwise returns existing SharedClassTokenHelper.</p>
	 * <p>Throws a HelperAlreadyDefinedException if the ClassLoader already has a different type of helper.</p>
	/*[IF JAVA_SPEC_VERSION < 24]
	 * <p>Returns null if a SecurityManager is installed and there is no
	 * SharedClassPermission for the ClassLoader specified.</p>
	/*[ENDIF] JAVA_SPEC_VERSION < 24
	 *
	 * @see SharedClassTokenHelper
	 *
	 * @param 		loader ClassLoader.
	 * 					ClassLoader to which this SharedClassTokenHelper will belong
	 *
	 * @return		SharedClassTokenHelper.
	 * 					A new or existing SharedClassTokenHelper
	 *
	 * @param 		filter SharedClassURLFilter.
	 * 					Specify a filter which limits the classes that are found or stored in the cache
	 *
	/*[IF JAVA_SPEC_VERSION == 8]
	 * @throws 		HelperAlreadyDefinedException
	 * 					when another helper has been already defined for this class loader
	/*[ENDIF] JAVA_SPEC_VERSION == 8
	 */
	public SharedClassTokenHelper getTokenHelper(ClassLoader loader, SharedClassFilter filter)
	/*[IF JAVA_SPEC_VERSION == 8]*/
			throws HelperAlreadyDefinedException
	/*[ENDIF] JAVA_SPEC_VERSION == 8 */
	;

	/**
	 * <p>Returns a SharedClassURLHelper for a given ClassLoader.</p>
	 * <p>Creates a new SharedClassURLHelper if one cannot be found, otherwise returns existing SharedClassURLHelper.</p>
	 * <p>Throws a HelperAlreadyDefinedException if the ClassLoader already has a different type of helper.</p>
	/*[IF JAVA_SPEC_VERSION < 24]
	 * <p>Returns null if a SecurityManager is installed and there is no
	 * SharedClassPermission for the ClassLoader specified.</p>
	/*[ENDIF] JAVA_SPEC_VERSION < 24
	 *
	 * @see SharedClassURLHelper
	 *
	 * @param 		loader ClassLoader.
	 * 					ClassLoader to which this SharedClassURLHelper will belong
	 *
	 * @return		SharedClassURLHelper.
	 * 					A new or existing SharedClassURLHelper
	 *
	/*[IF JAVA_SPEC_VERSION == 8]
	 * @throws 		HelperAlreadyDefinedException
	 * 					when another helper has been already defined for this class loader
	/*[ENDIF] JAVA_SPEC_VERSION == 8
	 */
	public SharedClassURLHelper getURLHelper(ClassLoader loader)
	/*[IF JAVA_SPEC_VERSION == 8]*/
			throws HelperAlreadyDefinedException
	/*[ENDIF] JAVA_SPEC_VERSION == 8 */
	;

	/**
	 * <p>Returns a SharedClassURLClasspathHelper for a given ClassLoader.</p>
	 * <p>Creates a new SharedClassURLClasspathHelper if one cannot be found,
	 *   otherwise if the classpath specified matches the classpath of an existing helper, returns existing SharedClassURLClasspathHelper.</p>
	 * <p>Throws a HelperAlreadyDefinedException if the ClassLoader already has a different type of helper, or
	 *   if the ClassLoader has a SharedClassURLClasspathHelper with a different classpath.</p>
	/*[IF JAVA_SPEC_VERSION < 24]
	 * <p>Returns null if a SecurityManager is installed and there is no
	 * SharedClassPermission for the ClassLoader specified.</p>
	/*[ENDIF] JAVA_SPEC_VERSION < 24
	 *
	 * @see SharedClassURLClasspathHelper
	 *
	 * @param 		loader ClassLoader.
	 * 					ClassLoader to which this SharedClassURLClasspathHelper will belong
	 *
	 * @param 		classpath URL[].
	 * 					The current URL classpath of this ClassLoader
	 *
	 * @return		SharedClassURLClasspathHelper.
	 * 					A new or existing SharedClassURLClasspathHelper
	 *
	 * @throws 		HelperAlreadyDefinedException
	/*[IF JAVA_SPEC_VERSION >= 9]
	 * 					when another SharedClassURLClasspathHelper has been already defined for this class loader
	/*[ELSE] JAVA_SPEC_VERSION >= 9
	 * 					when another helper has been already defined for this class loader
	/*[ENDIF] JAVA_SPEC_VERSION >= 9
	 */
	public SharedClassURLClasspathHelper getURLClasspathHelper(ClassLoader loader, URL[] classpath) throws HelperAlreadyDefinedException;

	/**
	 * <p>Returns a SharedClassURLClasspathHelper for a given ClassLoader.</p>
	 * <p>Creates a new SharedClassURLClasspathHelper if one cannot be found,
	 *   otherwise if the classpath specified matches the classpath of an existing helper, returns existing SharedClassURLClasspathHelper.</p>
	 * <p>If a new SharedClassHelper is created, the specified SharedClassURLFilter is applied to it. If the filter argument is null, no filter is applied.</p>
	 * <p>Throws a HelperAlreadyDefinedException if the ClassLoader already has a different type of helper OR
	 *   if the ClassLoader has a SharedClassURLClasspathHelper with a different classpath.</p>
	/*[IF JAVA_SPEC_VERSION < 24]
	 * <p>Returns null if a SecurityManager is installed and there is no
	 * SharedClassPermission for the ClassLoader specified.</p>
	/*[ENDIF] JAVA_SPEC_VERSION < 24
	 *
	 * @see SharedClassURLClasspathHelper
	 * @see SharedClassFilter
	 *
	 * @param 		loader ClassLoader.
	 * 					ClassLoader to which this SharedClassURLClasspathHelper will belong
	 *
	 * @param 		classpath URL[].
	 * 					The current URL classpath of this ClassLoader
	 *
	 * @param 		filter SharedClassURLFilter.
	 * 					Specify a filter which limits the classes that are found or stored in the cache
	 *
	 * @return		SharedClassURLClasspathHelper.
	 * 					A new or existing SharedClassURLClasspathHelper
	 *
	 * @throws 		HelperAlreadyDefinedException
	/*[IF JAVA_SPEC_VERSION >= 9]
	 * 					when another SharedClassURLClasspathHelper has been already defined for this class loader
	/*[ELSE] JAVA_SPEC_VERSION >= 9
	 * 					when another helper has been already defined for this class loader
	/*[ENDIF] JAVA_SPEC_VERSION >= 9
	 */
	public SharedClassURLClasspathHelper getURLClasspathHelper(ClassLoader loader, URL[] classpath, SharedClassFilter filter) throws HelperAlreadyDefinedException;

	/**
	 * <p>Returns a SharedClassURLHelper for a given ClassLoader.</p>
	 * <p>Creates a new SharedClassURLHelper if one cannot be found, otherwise returns existing SharedClassURLHelper.</p>
	 * <p>If a new SharedClassHelper is created, the specified SharedClassURLFilter is applied to it. If the filter argument is null, no filter is applied.</p>
	 * <p>Throws a HelperAlreadyDefinedException if the ClassLoader already has a different type of helper.</p>
	/*[IF JAVA_SPEC_VERSION < 24]
	 * <p>Returns null if a SecurityManager is installed and there is no
	 * SharedClassPermission for the ClassLoader specified.</p>
	/*[ENDIF] JAVA_SPEC_VERSION < 24
	 *
	 * @see SharedClassURLHelper
	 * @see SharedClassFilter
	 *
	 * @param 		loader ClassLoader.
	 * 					ClassLoader to which this SharedClassURLHelper will belong
	 *
	 * @param 		filter SharedClassURLFilter.
	 * 					Specify a filter which limits the classes that are found or stored in the cache
	 *
	 * @return		SharedClassURLHelper.
	 * 					A new or existing SharedClassURLHelper
	 *
	/*[IF JAVA_SPEC_VERSION == 8]
	 * @throws 		HelperAlreadyDefinedException
	 * 					when another helper has been already defined for this class loader
	/*[ENDIF] JAVA_SPEC_VERSION == 8
	 */
	public SharedClassURLHelper getURLHelper(ClassLoader loader, SharedClassFilter filter)
	/*[IF JAVA_SPEC_VERSION == 8]*/
			throws HelperAlreadyDefinedException
	/*[ENDIF] JAVA_SPEC_VERSION == 8 */
	;

	/**
	 * <p>Utility function that returns a SharedClassHelper for a given ClassLoader.</p>
	 * <p>Can be used to determine whether a given ClassLoader already has a helper, before calling a getter method.</p>
	 * <p>Returns an existing SharedClassHelper or null.</p>
	 *
	 * @see SharedClassHelper
	 *
	 * @param 		loader ClassLoader.
	 * 					ClassLoader which may or may not have a SharedClassHelper
	 *
	 * @return		SharedClassHelper.
	 * 					A helper if one exists for this ClassLoader or null otherwise.
	 *
	/*[IF JAVA_SPEC_VERSION >= 9]
	 *
	 * @throws 		IllegalStateException
	 * 					If this ClassLoader has more than one helper.
	 *
	 * @deprecated Use findHelpersForClassLoader(ClassLoader loader) instead.
	 *
	/*[ENDIF] JAVA_SPEC_VERSION >= 9
	 */
	/*[IF JAVA_SPEC_VERSION >= 9]*/
	@Deprecated(forRemoval=false, since="9")
	/*[ENDIF] JAVA_SPEC_VERSION >= 9 */
	public SharedClassHelper findHelperForClassLoader(ClassLoader loader);

	/*[IF JAVA_SPEC_VERSION >= 9]*/
	/**
	 * <p>Utility function that returns a set of SharedClassHelper for a given ClassLoader.</p>
	 * <p>Can be used to determine whether a given ClassLoader already has a specific helper, before calling a getter method.</p>
	 *
	 * @see SharedClassHelper
	 *
	 * @param 		loader ClassLoader.
	 * 					ClassLoader which may or may not have a SharedClassHelper
	 *
	 * @return		Set&lt;SharedClassHelper&gt;.
	 * 					A set of helpers exist for this ClassLoader.
	 */
	public Set<SharedClassHelper> findHelpersForClassLoader(ClassLoader loader);
	/*[ENDIF] JAVA_SPEC_VERSION >= 9 */

}
