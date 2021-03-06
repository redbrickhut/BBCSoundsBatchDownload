package org.menzies.view;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;


import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public final class NodeListDisplay<T> {

/*
* The aim of this class is to dynamically add nodes that are of the same view/fxml
* pairing, but require different view models. The Displays listens for changes to the
* list provided, and automatically adds and removes elements as necessary.
*/


    private final URL targetURL;
    private final Pane dropIn;
    private final HashMap<T, Node> viewModelReference;

    public NodeListDisplay(URL targetURL, Pane dropIn,
                           ObservableList<T> viewModels) {

        this.targetURL = targetURL;
        this.dropIn = dropIn;
        viewModelReference = new HashMap<>();
        viewModels.forEach(this::addNode);
        viewModels.addListener(this::onViewModelsChange);
    }

    private void onViewModelsChange(ListChangeListener.Change<? extends T> change) {

        while (change.next()) {
            change.getAddedSubList().forEach(this::addNode);
            change.getRemoved().forEach(this::removeNode);
        }
    }


    private void addNode(T viewModel) {

        FXMLLoader loader = new FXMLLoader(targetURL);
        Parent targetNode;

        try {
            targetNode = loader.load();
        } catch (IOException | IllegalStateException e) {
            targetNode = new Label("Error when loading from fxml");
            dropIn.getChildren().add(targetNode);
            return;
        }

        View<T> view = loader.getController();
        view.setVM(viewModel);
        viewModelReference.put(viewModel, targetNode);
        dropIn.getChildren().add(targetNode);
    }

    private void removeNode(T viewModel) {

        if (viewModelReference.containsKey(viewModel)) {

            Node target = viewModelReference.remove(viewModel);

            FadeTransition ft = new FadeTransition(Duration.millis(400), target);
            ft.setFromValue(1.0);
            ft.setToValue(0);
            ft.setCycleCount(1);
            ft.setAutoReverse(true);

            ft.play();

            ft.setOnFinished(e -> dropIn.getChildren().remove(target));
            ;
        }
        else System.err.println("Requested view model does not exist.");
    }

}
