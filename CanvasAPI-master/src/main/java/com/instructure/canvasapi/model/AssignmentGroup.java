package com.instructure.canvasapi.model;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Brady Larson
 *
 * Copyright (c) 2014 Instructure. All rights reserved.
 */
public class AssignmentGroup extends CanvasModel<AssignmentGroup> {

	private long id;
	private String name;
	private int position;
    private double group_weight;
	private List<Assignment> assignments = new ArrayList<Assignment>();

    ///////////////////////////////////////////////////////////////////////////
    // Getters and Setters
    ///////////////////////////////////////////////////////////////////////////

    @Override
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public List<Assignment> getAssignments() {
		return assignments;
	}
	public void setAssignments(List<Assignment> assignments) {
		this.assignments = assignments;
	}
    public double getGroupWeight() { return group_weight; }
    public void setGroupWeight(double group_weight) {this.group_weight = group_weight;}

    ///////////////////////////////////////////////////////////////////////////
    // Required Overrides
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public Date getComparisonDate() {
        return null;
    }

    @Override
    public String getComparisonString() {
        return getName();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////

    public AssignmentGroup() {}

    public AssignmentGroup(String name, int position) {
        this.name = name;
        this.id = name.hashCode();
        this.position = position;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.position);
        dest.writeList(this.assignments);
        dest.writeDouble(this.group_weight);
    }

    private AssignmentGroup(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.position = in.readInt();
        in.readList(this.assignments, Assignment.class.getClassLoader());
        this.group_weight = in.readDouble();
    }

    public static Creator<AssignmentGroup> CREATOR = new Creator<AssignmentGroup>() {
        public AssignmentGroup createFromParcel(Parcel source) {
            return new AssignmentGroup(source);
        }

        public AssignmentGroup[] newArray(int size) {
            return new AssignmentGroup[size];
        }
    };
}
