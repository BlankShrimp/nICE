package com.armpits.nice.db;

import com.armpits.nice.models.Deadline;
import com.armpits.nice.models.Material;
import com.armpits.nice.models.Module;

import java.util.List;

// this interface follows the delegation design pattern, which consists in
// performing object composition to achieve a sort of inheritance while keeping
// the context of the parent class
public interface DbHolder {
    public List<Module> getAllModules();
    public List<Deadline> getAllMDeadlines();
    public List<Material> getAllMaterial();
}
