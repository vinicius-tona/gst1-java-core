/* 
 * Copyright (c) 2008 Wayne Meissner
 * Copyright (C) 1999,2000 Erik Walthinsen <omega@cse.ogi.edu>
 *                    2000 Wim Taymans <wim.taymans@chello.be>
 *                    2005 Wim Taymans <wim@fluendo.com>
 * 
 * This file is part of gstreamer-java.
 *
 * This code is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * version 3 for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with this work.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.freedesktop.gstreamer.event;

import org.freedesktop.gstreamer.Event;
import org.freedesktop.gstreamer.TagList;
import org.freedesktop.gstreamer.lowlevel.ReferenceManager;

import com.sun.jna.Pointer;
import org.freedesktop.gstreamer.lowlevel.GstEventAPI;

/**
 * A new set of metadata tags has been found in the stream.
 */
public class TagEvent extends Event {

    private static final GstEventAPI gst = GstEventAPI.GSTEVENT_API;
    
    /**
     * This constructor is for internal use only.
     * @param init initialization data.
     */
    public TagEvent(Initializer init) {
        super(init);
    }
    /**
     * Creates a new TagEvent.
     * <p><b>Note:</b> This constructor takes ownership of the TagList.  Attempts to
     * access the TagList after passing it to this constructor will throw an exception.
     * 
     * @param taglist the taglist to transmit with the event.
     */
    public TagEvent(TagList taglist) {
        this(initializer(gst.ptr_gst_event_new_tag(taglist)));
    }
    
    /**
     * Gets the {@link TagList} stored in this event.
     * <p><b>Note:</b> The TagList is owned by the event, so it should only be 
     * accessed whilst holding a reference to this TagEvent.
     * 
     * @return the TagList stored in this event.
     */
    public TagList getTagList() {
        Pointer[] taglist = new Pointer[1];
        gst.gst_event_parse_tag(this, taglist);
        TagList tl = new TagList(taglistInitializer(taglist[0], false, false));
        ReferenceManager.addKeepAliveReference(tl, this);
        return tl;
    }
    private static Initializer taglistInitializer(Pointer ptr, boolean needRef, boolean ownsHandle) {
        if (ptr == null) {
            throw new IllegalArgumentException("Invalid native pointer");
        }
        return new Initializer(ptr, needRef, ownsHandle);
    }
}