/*******************************************************************************
 * Copyright IBM Corp. and others 2026
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
 *******************************************************************************/

#include "jfr.hpp"

#if defined(J9VM_OPT_JFR)

#include "j9.h"
#include "j9protos.h"
#include "mmprivatehook.h"
#include "mmomrhook.h"
#include "modronapi.hpp"

/* Forward declarations of hook handlers defined in vm/jfr.cpp */
extern "C" {
	void jfrOldGarbageCollection(J9HookInterface **hook, UDATA eventNum, void *eventData, void *userData);
	void jfrYoungGarbageCollection(J9HookInterface **hook, UDATA eventNum, void *eventData, void* userData);
	void jfrGarbageCollection(J9HookInterface **hook, UDATA eventNum, void *eventData, void *userData);
}

/**
 * Register GC-related JFR hooks.
 *
 * This function registers garbage collection related hooks for JFR event recording:
 * - Old generation GC cycles (OMR hook)
 * - Young generation GC cycles (OMR hook)
 * - General GC cycle completion (private hook)
 *
 * @param vm[in] The Java VM
 * @return 0 on success, non-zero on failure
 */
jint
jfrRegisterGCHooks(J9JavaVM *vm)
{
	J9HookInterface **gcOmrHooks = vm->memoryManagerFunctions->j9gc_get_omr_hook_interface(vm->omrVM);
	J9HookInterface **gcPrivateHooks = vm->memoryManagerFunctions->j9gc_get_private_hook_interface(vm);

	if ((*gcOmrHooks)->J9HookRegisterWithCallSite(gcOmrHooks, J9HOOK_MM_OMR_GC_CYCLE_END, jfrOldGarbageCollection, OMR_GET_CALLSITE(), NULL)) {
		return -1;
	}
	if ((*gcOmrHooks)->J9HookRegisterWithCallSite(gcOmrHooks, J9HOOK_MM_OMR_GC_CYCLE_END, jfrYoungGarbageCollection, OMR_GET_CALLSITE(), NULL)) {
		return -1;
	}
	if ((*gcPrivateHooks)->J9HookRegisterWithCallSite(gcPrivateHooks, J9HOOK_MM_PRIVATE_GC_POST_CYCLE_END, jfrGarbageCollection, OMR_GET_CALLSITE(), NULL)) {
		return -1;
	}

	return 0;
}

/**
 * Deregister GC-related JFR hooks.
 *
 * This function unregisters all garbage collection related hooks that were
 * previously registered for JFR event recording.
 *
 * @param vm[in] The Java VM
 */
void
jfrDeregisterGCHooks(J9JavaVM *vm)
{
	J9HookInterface **gcOmrHooks = vm->memoryManagerFunctions->j9gc_get_omr_hook_interface(vm->omrVM);
	J9HookInterface **gcPrivateHooks = vm->memoryManagerFunctions->j9gc_get_private_hook_interface(vm);

	(*gcOmrHooks)->J9HookUnregister(gcOmrHooks, J9HOOK_MM_OMR_GC_CYCLE_END, jfrOldGarbageCollection, NULL);
	(*gcOmrHooks)->J9HookUnregister(gcOmrHooks, J9HOOK_MM_OMR_GC_CYCLE_END, jfrYoungGarbageCollection, NULL);
	(*gcPrivateHooks)->J9HookUnregister(gcPrivateHooks, J9HOOK_MM_PRIVATE_GC_POST_CYCLE_END, jfrGarbageCollection, NULL);
}

#endif /* J9VM_OPT_JFR */
